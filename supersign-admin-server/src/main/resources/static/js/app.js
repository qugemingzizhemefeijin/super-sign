/*
 *
 *   INSPINIA - Responsive Admin Theme Custom
 *   version 2.4
 *
 */

$.extend( true, $.fn.dataTable.defaults, {
	rowId:'id',
	dom: '<"html5buttons"B>lTfgitpr',
	language : {
		info : '当前数据为从第  _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录',
		infoEmpty : '前数据为从第  0 到第 0 条数据',
		searchPlaceholder : '搜索关键词',
		search : '结果中查找:',
		lengthMenu : '每页显示 _MENU_ 行',
		infoFiltered : '(从 _MAX_ 条数据中过滤)',
		emptyTable : '噢哦，没有任何数据……',
		zeroRecords : '噢哦，没有匹配到数据……',
		processing : '数据正在加载中...',
		loadingRecords : '请稍等 - 数据加载中...',
		paginate : {
			first: "首页", 
			previous: "前一页", 
    		next: "后一页", 
    		last: "尾页"
		}
	},
	aoColumnDefs : [{
	    orderSequence: ["desc", "asc"],
        aTargets: ['_all']
	}],
   	buttons: [
      	{extend: 'copy',text:'拷贝'},
        {extend: 'csv'},
        {extend: 'excel', title: 'list'},
        {extend: 'pdf', title: 'list'},
        {extend: 'print',text:'打印',customize: function (win){
	        $(win.document.body).addClass('white-bg');
            $(win.document.body).css('font-size', '10px');
            $(win.document.body).find('table').addClass('compact').css('font-size', 'inherit');
        }}
    ]
});

var App = function () {
	//modal begin
	var closeAlertModel = function(){
		sweetAlert.close(-1);
		$('body').stop().modalmanager('removeLoading');
	};
	var closeModel = function(){
		$dialog.modal('hide');
		$('body').stop().modalmanager('removeLoading');
	};
	var modalDefConfirm = {confirmText : '确定',cancelText : '取消',title : '',type : 'info',confirm : closeAlertModel,cancel : closeAlertModel};
	var modalDefAlert = {confirmText : '确定',title : '',type : 'info',confirm : closeAlertModel};
	var modalDefHtml = {title : '提示',show:null,showTitle : true,showCloseButton : true,hide : null , width:760};
	var modalDefLoad = {title : '提示',show:null,showTitle : true,showCloseButton : true,hide : null , width:760};
	var $dialog = null,$dialog_header = null,$dialog_content = null;
	
	var handleDailog = function(){
		$.fn.modalmanager.defaults.resize = true;
		$.fn.modalmanager.defaults.spinner = '<div class="loading-spinner fade" style="width: 200px; margin-left: -100px;"><div class="sk-spinner sk-spinner-three-bounce"><div class="sk-bounce1"></div><div class="sk-bounce2"></div><div class="sk-bounce3"></div><div class="sk-bounce4"></div><div class="sk-bounce5"></div></div></div>';
		
		if($("#dialog").length==0) $("body").append('<div class="modal inmodal" id="dialog" tabindex="-1" data-backdrop="static" data-keyboard="true" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content animated fadeIn"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button><h4 class="modal-title">标题</h4></div><div class="model-main"></div></div></div></div>');
		$dialog=$("#dialog");
		$dialog.on("hidden", function() {
		    $(this).removeData("modal").off('show').off('hide');
		});
		$dialog_header = $dialog.find('.modal-header');
		$dialog_content= $dialog.find(".model-main");
	}
	
	var handleToastr = function(){
		toastr.options = {closeButton: true,progressBar: true,showMethod: 'slideDown',timeOut: 4000};
	}
	
	var Core = {
		ajax: jQuery.ajax
	};
	
	var handleAjax = function(){
		function interceptor(d, s, r, v){
			var x = r.getResponseHeader("ZXRTB-AJAX");
			if(x=='0'){
				closeModel();
				App.Modal.alert(d.length==0?'您当前的登录信息已过期，请重新登录！':d,{confirm : function(){
					var returnURL = App.Tools.currentPageURL();
					var url = _contextPath + "/login";
					if(returnURL != "") {
						url += "?redirectURL="+encodeURIComponent(returnURL);
					}
					top.location.href=url;
				}});
			}else if(x=='1'){
				closeModel();
				App.Modal.alert(d);
			}else if(x=="2"){
				App.Modal.close();
				try{
					new Function(d)();
				}catch(e2){
					if(s.error)s.error.call(this, r, "error", e2);
				}
			}else if(x=="3"){
				window.location.href=d;
			}else if(x=="4"){
				window.location.reload();
			}else if(s.success){
				s.success.call(this, d, v, r);
			}
		}
		function doAjaxSuccess(d, s, r, v) {
			if(r){
				interceptor(d,s,r,v);
			}else{
				if(s.success)s.success.call(this, d, v, r);
			}
		}
		
		function doAjaxBeforeSend(s , r , o){
    		console.log(o.url);
			if(o.url.indexOf("_ajax")==-1) {//没有ajax
    			o.url = o.url + (o.url.indexOf('?')>-1?"&":"?") +"_ajax=true";
    		}
    		if(s.beforeSend) {
    			s.beforeSend.call(this , r , o);
    		}
		}
		function doAjaxError(s , xhr, error, thrown) {
			console.log("================"+xhr.responseText);
			var x = xhr.getResponseHeader("ZXRTB-AJAX");
			if(x != '') {
				interceptor(xhr.responseText,s,xhr,error);				
			} else {
				if(s.error) {
					s.error.call(this , xhr , error , thrown);
				}
			}
		}
	    
	    jQuery.ajax = function (s) {
	    	if (!s){return Core.ajax();}
	    	var r = null;
	    	var n_s = $.extend({}, s);
	    	if (n_s.context) throw new Error("不支持context选项。");
	    	n_s.success = function (d, v, r2) {doAjaxSuccess.call(this, d, s, r2 || r, v)};
	    	n_s.beforeSend = function(r , o){doAjaxBeforeSend.call(this , s , r , o)};
	    	n_s.error = function(x,e,t){doAjaxError.call(this,s,x,e,t)};
	    	r = Core.ajax(n_s);
	    	return r;
	    };
	}
	
    //Begin Ajax
    var ajaxDefOpts = {success : null , error : null,complete : null , sendTips : 1 , succTips : 1};
    handleAjax();
    //End Ajax
    
    var handleCheckbox = function(){
    	var checkAll = $('.checkall,.checkall_two');
    	checkAll.change(function () {
        	var checked = $(this).is(":checked");
        	checkAll.prop('checked',checked);
        	if(checked) {
        		$('.checkchild').prop('checked',checked).parents('tr').addClass('selected');
        	} else {
        		$('.checkchild').prop('checked',checked).parents('tr').removeClass('selected');
        	}
    	});
    }
	
	//modal end
	return {
		init : function(){
			handleDailog();//dialog
			handleToastr();//Toast
			handleCheckbox();
		},
		Ajax : {
			submit : function(formid,options){
				var opt = $.extend({},ajaxDefOpts, options);
				var form = $('#'+formid);
            	if(form.length == 0 || form.data('send')=='true') return;
            	form.data('send','true');
            	
            	var url = form.attr('action');
            	var data = form.serializeArray();
            	if(!data || typeof data != 'object') data = {};
            	data['_ajax']='true';
            	
            	var options = {
            		"url":  url,
            		"data": data,
            		"dataType": "json",
            		"cache": false,
            		"type": 'post',
            		"beforeSend" : function(){
            	    	if(opt.sendTips == 1) App.Tips.show('数据正在处理......');
            		},
            		"success": function(json){
            			App.Ajax.resolve(json , opt);
            		},
            		"complete":function(xhr,status){
            			form.data('send','false');
            			if(opt.complete && opt.complete instanceof Function) opt.complete();
                    },
            		"error": function (xhr, error, thrown) {
            			App.Tips.show('数据提交发生错误......');
            		}
            	}
                if(form.attr('enctype') == 'multipart/form-data'){
                	form.ajaxSubmit(options);
                }else{
                	$.ajax(options);
            	}
			},
			get : function(url, options){
            	url = url.replace(new RegExp("_ajax=([^\&]*)[\&]*","g"),'');
            	url = url.replace(new RegExp("[\&|\?]+$"),'');
            	if(url.indexOf('_ajax') == -1){
            		if(url.indexOf('?') != -1)  url += '&_ajax=true';
            		else url += '?_ajax=true'
            	}
            	
            	var opt = $.extend({},ajaxDefOpts, options);
            	$.ajax({
    				"url":  url,
    				"dataType": "json",
    				"cache": false,
    				"type": 'get',
    				"beforeSend" : function(){
    	            	if(opt.sendTips == 1) App.Tips.show('数据正在处理......');
    				},
    				"success": function(json){
    					App.Ajax.resolve(json,opt);
    				},
    				"complete":function(xhr,status){
    					if(opt.complete && opt.complete instanceof Function) opt.complete();
              		},
    				"error": function (xhr, error, thrown) {
    					App.Tips.show('数据提交发生错误......');
    				}
    			});
            },
            post : function(url, data,options){
            	if(!data || typeof data != 'object') data = {};
            	data['_ajax']='true';
            	
            	var opt = $.extend({},ajaxDefOpts, options);
            	
            	$.ajax({
    				"url":  url,
    				"data":data,
    				"dataType": "json",
    				"cache": false,
    				"type": 'post',
    				"beforeSend" : function(){
    	            	if(opt.sendTips == 1) App.Tips.show('数据正在处理......');
    				},
    				"success": function(json){
    					App.Ajax.resolve(json,opt);
    				},
    				"complete":function(xhr,status){
    					if(opt.complete && opt.complete instanceof Function) opt.complete();
              		},
    				"error": function (xhr, error, thrown) {
    					App.Tips.show('数据提交发生错误......');
    				}
    			});
            },
            resolve : function(json,opt){
            	var fn = opt.success , errfn = opt.error;
            	if(json.success){
            		if(opt.succTips == 1) App.Tips.success(json.msg);
            		
            		if(json.resultType){
            			switch(json.resultType){
        					case 1://刷新本页面
        						window.location.reload();
        						break;
            				case 2://执行函数
            					if(fn && fn instanceof Function){
            						fn(json);
                    			}
            					break;
            				case 3://关闭窗口并且刷新页面
            					App.Modal.close();
            					window.location.reload();
            					break;
            				case 10 ://关闭窗口并且刷新表格
            				case 11 ://关闭窗口并且刷新表格当前页
            					App.Modal.close();
            					App.Tables.refresh('dataTable',json.resultType==10?true:false);
            					$('.checkall,.checkall_two').prop('checked',false);
            					break;
            				case 12:
            					window.opener.window.App.Tables.refresh('dataTable',false);
            					window.close();
            					break;
            				case 4://关闭窗口并且执行函数
            					if(fn && fn instanceof Function){
            						fn(json);
                    			}
            					App.Modal.close();
            					break;
            				case 5://关闭窗口
            					App.Modal.close();
            					break;
            				case 6://跳转到指定页面
            					window.location = json.data;
            					break;
            				case 7://什么都不做
            					break;
            				case 8://关闭本窗口并且刷新父窗口
            					 window.opener.location.reload();
            	                 window.close();
            					break;
            				case 9://关闭本页面
            					window.close();
            					break;
            			}
            		}
            	}else{
            		App.Tips.error(json.msg);
            		if(json.resultType == 4){
            			if(errfn && errfn instanceof Function){
            				errfn(json);
            			}
            		}
            	}
            }
		},
		Tips : {
			show : function(message){
				toastr.clear();
        		toastr.info(message , '' , {
        			timeOut : 0
        		});
			},
			warning : function(message){
				toastr.clear();
				toastr.warning(message);
        	},
        	info : function(message){
        		toastr.remove();
        		toastr.info(message);
        	},
        	success : function(message){
        		toastr.clear();
        		toastr.success(message);
        	},
        	error : function(message){
        		toastr.clear();
        		toastr.error(message);
        	}
		},
		Modal : {
			html : function(modalBody , options , modalFooter){
				closeAlertModel();
				var opt = $.extend({} , modalDefHtml, options);
        		$dialog_content.html('');
        		
        		$dialog_content.html('<div class="modal-body">'+modalBody+'</div>');
        		if(typeof modalFooter == 'string') $dialog_content.append('<div class="modal-footer">'+modalFooter+'</div>');
        		
        		if(!opt.showTitle) $dialog_header.hide();
        		else {
        			if(!opt.showCloseButton) {
        				$dialog_header.find('button').hide();
        			} else {
        				$dialog_header.find('button').show();
        			}
        			$dialog_header.find('.modal-title').text(opt.title);
        			$dialog_header.show();
        		}
        		
        		if(opt.show != null && typeof opt.show == 'function') $dialog.off('show').on('show',opt.show);
        		if(opt.hide != null && typeof opt.hide == 'function') $dialog.off('hide').on('hide',opt.hide);
        		//$dialog.attr({'data-keyboard':'true','data-width':opt.width}).css({'width':opt.width+'px'}).modal({keyboard:true,width:opt.width,backdrop:'static'});
        		$dialog.children('.modal-dialog').css({'width':opt.width+'px'});
        		$dialog.attr({'data-keyboard':'true'}).modal({keyboard:true,backdrop:'static'});
			},
			show : function(url , params , options) {
				var _dialog = $('<div class="modal inmodal" tabindex="-1" data-backdrop="static" data-backdrop="false" data-keyboard="true" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content animated fadeIn"><div class="modal-header"><button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button><h4 class="modal-title">标题</h4></div><div class="model-main"></div></div></div></div>');
				var elements = $('body');
				elements.append(_dialog);
				
				var isbackdrop = !(elements.children('.modal-backdrop').length>0);
				
				_dialog.on('hide', function () {
					var zindex = elements.children('.modal-scrollable:last').css('z-index');
					elements.children('.modal-backdrop').css('z-index' , parseInt(zindex)-15);
				}).on('show', function () {
					var zindex = elements.children('.modal-scrollable:last').css('z-index');
					elements.children('.modal-backdrop').css('z-index' , parseInt(zindex)-5);
				}).on("hidden", function() {
					setTimeout(function(){_dialog.empty().remove();},50);
				});
				
				var _dialog_header = _dialog.find('.modal-header');
				var _dialog_content= _dialog.find(".model-main");
				
				if(!params || typeof params != "object") params = {};
        		params['_ajax']='true';
        		
        		$.ajax({
    				"url":  url,
    				"data": params,
    				"dataType": "html",
    				"cache": false,
    				"type": 'post',
    				"beforeSend" : function(){
    					elements.modalmanager('loading');
    				},
    				"success": function(responseText){
    					_dialog_content.html(responseText);

    					var opt = $.extend({},modalDefLoad,options?options:{});
    					//查看里面是否设置了width
                		var modal_body = _dialog_content.children('.modal-body');
                		var dataWidth = modal_body.attr('data-width');
                		if(dataWidth && App.isMath(dataWidth)){
                			opt.width = parseInt(dataWidth);
                		}
                		var dataTitle = modal_body.attr('data-title');
                		if(dataTitle && dataTitle != ''){
                			opt.title = dataTitle;
                		}
                		
                		if(!opt.showTitle) _dialog_header.hide();
                		else {
                			if(!opt.showCloseButton) {
                				_dialog_header.find('button').hide();
                			} else {
                				_dialog_header.find('button').show();
                			}
                			_dialog_header.find('.modal-title').text(opt.title);
                			_dialog_header.show();
                		}
                		
                		if(opt.show != null && typeof opt.show == 'function') _dialog.off('show').on('show',opt.show);
                		if(opt.hide != null && typeof opt.hide == 'function') _dialog.off('hide').on('hide',opt.hide);
                		_dialog.children('.modal-dialog').css({'width':opt.width+'px'});
                		_dialog.attr({'data-keyboard':'true'}).modal({keyboard:true,backdrop:isbackdrop});
    				},
    				"complete":function(xhr,status){
    					elements.stop().modalmanager('removeLoading');
              		},
    				"error": function (xhr, error, thrown) {
    					elements.stop().modalmanager('removeLoading');
    					alert(xhr.responseText);
    					_dialog.remove();
    				}
    			});
			},
			load : function(url,params,options){
				/*if(true) {
					$('body').modalmanager('loading');
					return;
				}*/
				closeAlertModel();
        		$dialog_content.html('');
        		if(!params || typeof params != "object") params = {};
        		params['_ajax']='true';
        		var elements = $('body');
        		
        		$.ajax({
    				"url":  url,
    				"data": params,
    				"dataType": "html",
    				"cache": false,
    				"type": 'post',
    				"beforeSend" : function(){
    					elements.modalmanager('loading');
    				},
    				"success": function(responseText){
    					$dialog_content.html(responseText);

    					var opt = $.extend({},modalDefLoad,options?options:{});
    					//查看里面是否设置了width
                		var modal_body = $dialog_content.children('.modal-body');
                		var dataWidth = modal_body.attr('data-width');
                		if(dataWidth && App.isMath(dataWidth)){
                			opt.width = parseInt(dataWidth);
                		}
                		var dataTitle = modal_body.attr('data-title');
                		if(dataTitle && dataTitle != ''){
                			opt.title = dataTitle;
                		}
                		
                		if(!opt.showTitle) $dialog_header.hide();
                		else {
                			if(!opt.showCloseButton) {
                				$dialog_header.find('button').hide();
                			} else {
                				$dialog_header.find('button').show();
                			}
                			$dialog_header.find('.modal-title').text(opt.title);
                			$dialog_header.show();
                		}
                		
                		if(opt.show != null && typeof opt.show == 'function') $dialog.off('show').on('show',opt.show);
                		if(opt.hide != null && typeof opt.hide == 'function') $dialog.off('hide').on('hide',opt.hide);
                		//$dialog.attr({'data-keyboard':'true'}).css({'width':opt.width+'px'}).modal({keyboard:true,width:opt.width,backdrop:'static'});
                		$dialog.children('.modal-dialog').css({'width':opt.width+'px'});
                		$dialog.attr({'data-keyboard':'true'}).modal({keyboard:true,backdrop:'static'});
    				},
    				"complete":function(xhr,status){
    					elements.stop().modalmanager('removeLoading');
              		},
    				"error": function (xhr, error, thrown) {
    					elements.stop().modalmanager('removeLoading');
    					alert(xhr.responseText);
    				}
    			});
        	},
        	close : function(){
        		//$dialog.modal('hide');
        		$('body').children('.modal-scrollable:last').children('.modal').modal('hide');
        		closeAlertModel();
        	},
			closeAlert : closeAlertModel,
			confirm : function(msg , options){
				var opt = $.extend({},modalDefConfirm, options);
				swal({
	                title: opt.title,
	                text: msg,
	                type: opt.type,
	                showCancelButton: true,
	                cancelButtonText : opt.cancelText,
	                confirmButtonColor: "#DD6B55",
	                confirmButtonText: opt.confirmText,
	                closeOnConfirm: false,
	                closeOnCancel: false
	            }, function (isConfirm) {
	            	if (isConfirm) {
	            		opt.confirm();
                    } else {
                    	opt.cancel();
                    }
	            });
			},
			success : function(msg , options){
				var opt = $.extend({},modalDefAlert, options , {type : 'success'});
				this.tips(msg , opt);
			},
			warning : function(msg , options){
				var opt = $.extend({},modalDefAlert, options , {type : 'warning'});
				this.tips(msg , opt);
			},
			error : function(msg , options){
				var opt = $.extend({},modalDefAlert, options , {type : 'error'});
				this.tips(msg , opt);
			},
			info : function(msg , options){
				var opt = $.extend({},modalDefAlert, options);
				this.tips(msg , opt);
			},
			alert : function(msg , options){
				this.info(msg , options);
			},
			tips : function(msg , options) {
				swal({
	                title: options.title,
	                text: msg,
	                type: options.type,
	                closeOnConfirm : false,
	                confirmButtonText : options.confirmText
	            }, options.confirm);
			}
		},
        Tools : {
        	getActualVal: function (el) {
                var el = jQuery(el);
                if (el.val() === el.attr("placeholder")) {
                    return "";
                }
                return el.val();
            },
            getURLParameter: function (paramName) {
                var searchString = window.location.search.substring(1),i, val, params = searchString.split("&");
                for (i = 0; i < params.length; i++) {
                    val = params[i].split("=");
                    if (val[0] == paramName) {
                        return unescape(val[1]);
                    }
                }
                return null;
            },
        	trimText : function(form) {
        		var sfind = "";
        		if(typeof form == 'undefined'){
        			sfind = "input[type='text']";
        		}else{
        			sfind = "#"+form+" input[type='text']";
        		}
        		
        		$(sfind).each(function() {
        			var ss = $(this).attr("value");
        			jQuery(this).attr("value", jQuery.trim(ss));
        		});
        	},
        	href : function(url , param){
        		if(url.indexOf('?') > -1){
        			url = url + "&";
        		}else{
        			url = url + "?";
        		}
        		
        		$.each(param , function(k,v){
        			url += k + "="+v+"&";
        		});
        		
        		top.location.href=url.substring(0,url.length-1);
        	},
        	open : function(url , param){
        		if(url.indexOf('?') > -1){
        			url = url + "&";
        		}else{
        			url = url + "?";
        		}
        		
        		$.each(param , function(k,v){
        			url += k + "="+v+"&";
        		});
        		
        		window.open(url.substring(0,url.length-1));
        	},
        	getCurrentChecked : function(){
    			var box = $('.checkchild:checked');
    			if(box.length == 0) {
    				return '';
    			}
    			var ids = new Array();
    			box.each(function(){
    				ids.push(this.value);
    			});
    			return ids.join(',');
    		},
    		currentPageURL : function(){//当前页面的URL不包含http和域名
    			var C_URL_LOACTION = ""+document.location;
    			C_URL_LOACTION = C_URL_LOACTION.replace("http://","");
    			C_URL_LOACTION = C_URL_LOACTION.substring(C_URL_LOACTION.indexOf("/"));
    			return C_URL_LOACTION;
    		},
    		startDatePicker : function(v){
    			if(v == '') {
    				return moment();
    			}
    			return v;
    		},
    		endDatePicker : function(v){
    			if(v == '') {
    				return moment().add(6, "days");
    			}
    			return v;
    		},
            rangeDatePickerRight : {
                format: "YYYY-MM-DD",
                startDate: moment(),
                endDate: moment().add(6, "days"),
                minDate: "2016-11-15",
                maxDate: "2050-11-15",
                dateLimit: {
                    days: 4650
                },
                showDropdowns: !1,
                showWeekNumbers: !1,
                timePicker: !1,
                timePickerIncrement: 1,
                timePicker12Hour: !1,
                ranges: {
                    "今天": [moment(), moment()],
                    "昨天": [moment().subtract(1, "days"), moment().subtract(1, "days")],
                    "最近一周": [moment().subtract(7, "days"), moment().subtract(1, "days")],
                    "本月": [moment().startOf("month"), moment().endOf("month")],
                    "上月": [moment().subtract(1, "month").startOf("month"), moment().subtract(1, "month").endOf("month")]
                },
                opens: "right",
                drops: "down",
                buttonClasses: ["btn", "btn-sm"],
                applyClass: "btn-success",
                cancelClass: "btn-default",
                separator: " 到 ",
                locale: {
                    applyLabel: "确定",
                    cancelLabel: "取消",
                    fromLabel: "开始",
                    toLabel: "结束",
                    customRangeLabel: "自定义",
                    daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                    monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                    firstDay: 1
                }
            }
        },
        // wrapper function to  block element(indicate loading)
        blockUI: function (el, centerY) {
            var el = jQuery(el); 
            el.block({
                    message: '<div class="sk-spinner sk-spinner-rotating-plane"></div>',
                    centerY: centerY != undefined ? centerY : true,
                    css: {
                        top: '10%',
                        border: 'none',
                        padding: '2px',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: '#000',
                        opacity: 0.05,
                        cursor: 'wait'
                    }
                });
        },
        // wrapper function to  un-block element(finish loadiwng)
        unblockUI: function (el) {
            jQuery(el).unblock({
                    onUnblock: function () {
                        jQuery(el).removeAttr("style");
                    }
                });
        },
        Tables : {
        	get : function(key){
        		return $.data(document.body, key+'_Tables');
        	},
        	set : function(key,value){
        		$.data(document.body, key+'_Tables' , value);
        	},
        	refresh : function(key,bo){
        		$('.checkall').prop('checked',false);
        		var tb = this.get(key);
        		if(tb) tb.draw(bo === false ? false : true);
        	}
        },
		isMath : function(s){
        	if(s==""){return false;}
        	if(/^\d+$/.test(s)){return true;}else{return false;}
        },
        isMobile : function(a){
        	var b=new RegExp("^1[0-9]{10}$");return b.test(a);
        },
        isMoney : function(v){
        	var patrn=/^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,2})?$/;
        	if(patrn.exec(v)) return true;
        	return false;
        },
        isEmail : function(v){
        	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?[-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        	return reg.test(v);
        },
        serialize : function(form){
        	var fnames = {};
        	var filterElements = $('#'+form).find('.filter_seria');
        	if(filterElements.length > 0) {
        		filterElements.each(function(){
        			var that = $(this);
        			if(that.attr('name')) fnames[that.attr('name')]=1;
        		});
        	}
        	var data = {};
        	$.each($('#'+form).serializeArray() , function(i,v){
        		if(v.value != '' && typeof fnames[v.name] == 'undefined') {
        			if(data[v.name]) data[v.name] = data[v.name]+','+v.value;
        			else data[v.name] = v.value;
        		}
        	});
        	return data;
        },
        Condition : {
        	//这个方法是在页面加载的时候调用的，传入select的ID和URI,可以放在datatables初始化的代码的下面执行即可
        	list : function(queryId , uri){
        		App.Ajax.post('下同',{uri : uri} , {
        			sendTips : 0,
        			succTips : 0,
        			success : function(json){
        				var cod = $('#'+queryId);
        				cod.empty().append('<option value="">请选择</option>');
        				for(var i=0;i<json.data.length;i++){
        					cod.append('<option value="'+json.data[i].id+'">'+json.data[i].name+'</option>');
        				}
        			}
        		});
        	},
        	//此方法是在你的弹出页面点击保存调用的方法，调用房还是App.Condition.save();
        	//保存查询条件,queryId是你的html select查询列表框，uri就是你的页面的uri路径，自己想办法传入到你的弹出页面中接口,name名称，form不填写则默认为search_form
        	save : function(queryId , param , form){
        		if(typeof form == 'undefined') form = 'search_form';
        		var jdata = JSON.stringify(App.serialize(form));
        	//	App.Ajax.post('此处地址是一个通用的保存地址,单独写一个controller最好' , {'uri':uri,'name':name,'data':jdata} , {
        		App.Ajax.post('/cost/query/ajax/addsave' , {'uri':param.uri,'name':param.name,'data':jdata} , {
        			sendTips : 0,
        			succTips : 0,
        			success : function(json){
        				//ActionResult的success必须要调用函数并且关闭窗口的那个方法，ActionResult 168行的那个方法
        				//data可以传入一个map,id:新添加的ID,name:名称
        				$('#'+queryId).find(':first').after('<option value="'+json.data.id+'">'+json.data.name+'</option>');
        				if((typeof param.callback).toLowerCase() == 'function') {
        					param.callback(json);
        				}
        			}
        		});
        	},
        	//调用删除查询条件的删除方法
        	remove : function(queryId , conditionId){
        		App.Ajax.post('/cost/query/ajax/delect',{conditionId : conditionId} , {
        			sendTips : 0,
        			succTips : 0,
        			success : function(json){
        				$('#'+queryId).find("options[value='"+conditionId+"']").remove();
        			}
        		});
        	},
        	//此方法是加在select的onchange事件上面的
        	change : function(o , form){
        		var v = $(o).val();
        		if(typeof form == 'undefined') form = 'search_form';
        		
        		if(v == ''){
        			$('#'+form).get(0).reset();
        			$(o).find(':first').prop('selected',true);
        			var selectMultiple = $('#'+form).find("select[multiple='multiple']");
        			if(selectMultiple.length > 0) {
        				selectMultiple.each(function(){
        					$(this).trigger("chosen:updated");
        				});
        			}
        			return;
        		}
        		
        		App.Ajax.post('/cost/query/ajax/getdata' , {conditionId : v} , {
        			sendTips : 0,
        			succTips : 0,
        			success : function(json){
        				//这个方法是最难的。。。。
        				var data = JSON.parse(json.data);
        				App.Condition.fillData(form , data);
        			}
        		});
        	},
        	fillData : function(form , data){
        		//首先将所有的input和select都获取
        		var inputs = $('#'+form).find('input,select,checkbox');
        		if(inputs.size() == 0) return;
        		inputs.filter('input,select').not('.filter_seria').val('');
        		inputs.filter('checkbox').not('.filter_seria').prop('checked',false);
        		
        		inputs.each(function(i){
        			//这里先判断类型，再获取名称，在判断data里面是否有此数据
        			var that = $(this);
        			var tag = that[0].tagName.toLowerCase();
        			var name = that.attr('name');
        			//alert(tag+"--"+name);
        			if(typeof data[name] != 'undefined') {
        				switch(tag) {
	        				case 'input' :
	        					that.val(data[name]);
	        					break;
	        				case 'select' :
	        					if(that.attr('multiple')=='multiple') {
	        						var s = data[name].split(',');
	        						if(s != null && s.length > 0) {
	        							for(var i=0;i<s.length;i++) {
	        								that.find("option[value='"+s[i]+"']").prop("selected", true);
	        							}
	        							that.trigger("chosen:updated");
	        						}
	        					} else {
	        						that.val(data[name]);
	        					}
	        					break;
	        				case 'checkbox' :
	        					if(that.val()==data[name]) {
	        						that.prop('checked',true);
	        					}
	        					break;
	        			}
					}
        		});
        	}
        }
	};
}();

/* jquery validator plugins */
jQuery.extend(jQuery.validator.messages, {
    required: "必填字段",
	remote: "请修正该字段",
	email: "请输入正确格式的电子邮件",
	url: "请输入合法的网址",
	date: "请输入合法的日期",
	dateISO: "请输入合法的日期 (ISO).",
	number: "请输入合法的数字",
	digits: "只能输入整数",
	creditcard: "请输入合法的信用卡号",
	equalTo: "请再次输入相同的值",
	accept: "请输入拥有合法后缀名的字符串",
	maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
	minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
	rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
	range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
	min: jQuery.validator.format("请输入一个最小为 {0} 的值")
});

//匹配邮政编码
jQuery.validator.addMethod("isZipCode", function(value, element) {
var pattern = /^[0-9]{6}$/;
if(this.optional(element) || (pattern.test(value))) {
     //$(element).attr("class","input_txt");
    return true;
}else {
    return false;
}
}, "请正确填写您的邮政编码");
//匹配由26个英文字母组成的字符串
jQuery.validator.addMethod("isLetter", function(value, element) {
var pattern = new RegExp("^[A-Za-z]+$");
if(this.optional(element) || (pattern.test(value))) {
    return true;
}else {
    return false;
}
},"请填写只由英文组成的字符串");
//手机号码验证
jQuery.validator.addMethod("isMobile", function(value, element) {
 var length = value.length;
 var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
 if(this.optional(element) || (length == 11 && mobile.test(value))) {
    return true;
}else {
    return false;
}
}, "请正确填写您的手机号码");

// 电话号码验证  电话号码格式010-12345678
jQuery.validator.addMethod("isTel", function(value, element) {
 var tel = /^(\d{3,4}-)?\d{7,9}$/;
 if(this.optional(element) || (tel.test(value))) {
    return true;
}else {
    return false;
}
}, "请正确填写您的电话号码");
// 联系电话(手机/电话皆可)验证
jQuery.validator.addMethod("isPhone", function(value,element) {
 var length = value.length;
 var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
 var tel = /^(\d{3,4}-)?\d{7,9}$/;

 if(this.optional(element) || (tel.test(value) || (length == 11 && mobile.test(value)))) {
    return true;
}else {
    return false;
}

}, "请正确填写您的联系电话");
//验证括中文字、英文字母、数字和下划线
jQuery.validator.addMethod("stringCheck", function(value, element) {
 if(this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value)) {
    return true;
}else {
    return false;
}
}, "只能包括中文字、英文字母、数字和下划线");
//验证金额 小数点后面可以跟0到2位
jQuery.validator.addMethod("moneyCheck", function(value, element) {
 if(this.optional(element) || /^[1-9][0-9]*(\.)?([0-9]){0,2}$|^[0-9](\.)?([0-9]){0,2}$/.test(value)) {
    return true;
}else {
    return false;
}
}, "金额格式错误");
//验证是否是金额 小数点后面可以跟0到2位，可以为负数
jQuery.validator.addMethod("moneyCheck2", function(value, element) {
 if(this.optional(element) || /^[-]?[1-9][0-9]*(\.)?([0-9]){0,2}$|^[-]?[0-9](\.)?([0-9]){0,2}$/.test(value)) {
    return true;
}else {
    return false;
}
}, "金额格式错误");
//验证email
jQuery.validator.addMethod("isMail", function(value, element) {
if(this.optional(element) || /^[A-Za-z0-9]+([-_.][A-Za-z0-9]+)*@([A-Za-z0-9]+[-.])+[A-Za-z0-9]{2,5}$/.test(value)) {
    return true;
}else {
    return false;
}
}, "只能包括英文字母、数字和下划线");
//是有效地整数
jQuery.validator.addMethod("isInt", function(value, element) {
if(this.optional(element) || (/^([1-9][0-9]*)$/.test(value) && value*1 < 1000000000)) {
    return true;
}else {
    return false;
}
}, "请输入有效整数");
//是有效地数字
jQuery.validator.addMethod("isNumber", function(value, element) {
if(this.optional(element) || (/^([0-9][0-9]*)$/.test(value))) {
    return true;
}else {
    return false;
}
}, "请输入有效的数字");
//大于0的金额
jQuery.validator.addMethod("checkPriceNoZero", function(value, element) {
	if(this.optional(element) || /^[1-9][0-9]*(\.)?([0-9]){0,2}$|^[0-9](\.)?([0-9]){0,2}$/.test(value)) {
		if(parseFloat(value) <= 0) return false;
		return true;
	}else {
	    return false;
	}
}, "请输入正确的金额并且必须大于0");
//验证整数
jQuery.validator.addMethod("checkPriceNoFj", function(value, element) {
return this.optional(element) || /^([1-9][0-9]*)$|^0$/.test(value) || /^(?!0(\.0+)?$)([1-9][0-9]{0,9}|0)(\.[0]{1,3})?$/.test(value);
}, "金额只能是正整数");
//比例
jQuery.validator.addMethod("checkScale", function(value, element) {
	if(this.optional(element) || /^0(\.)?([0-9]){0,2}$/.test(value)) {
		return true;
	}else {
	    return false;
	}
}, "比例格式错误");
//是有大于零的效地整数
jQuery.validator.addMethod("isMaxZeroInt", function(value, element) {
return this.optional(element) || /^[1-9][0-9]*$/.test(value);
}, "请输入大于零整数");
//是有效地重量,小数点后三位 大于0
jQuery.validator.addMethod("isWeight", function(value, element) {
return this.optional(element) || /^(?!0(\.0+)?$)([1-9][0-9]{0,9}|0)(\.[0-9]{1,3})?$/.test(value);
}, "请输入有效的重量");

jQuery.validator.addMethod("zh_CnLength",function(value,element,param){
value = value.replace(/(^\s*)|(\s*$)/g, "");
String.prototype.getBytes = function() {
    var cArr = this.match(/[^\x00-\xff]/ig);
    return this.length + (cArr == null ? 0 : cArr.length);
}
var length = value.getBytes();
if(length>param){
    return false;
}
return true;
},"请输入一个长度最多是 {0} 的字符串,一个中文算两个长度");

jQuery.validator.addMethod("selectRequired", function(value, element) {
var value = $(element).val() + '';
if(value == '' || value == '0' || value == '-1' || value == '请选择') {
    return false;
}
return true;
}, "请选择选项");

jQuery.validator.addMethod("isImg", function(value, element) {
if(value == '') {
    return true;
}

//为了避免转义反斜杠出问题，这里将对其进行转换
var re = /(\\+)/g;
var filename=value.replace(re,"#");
//对路径字符串进行剪切截取
var one=filename.split("#");
//获取数组中最后一个，即文件名
var two=one[one.length-1];
//再对文件名进行截取，以取得后缀名
var three=two.split(".");
//获取截取的最后一个字符串，即为后缀名
var last=three[three.length-1];

//添加需要判断的后缀名类型
var tp ="jpg,png,gif,jpeg";
//返回符合条件的后缀名在字符串中的位置
var rs=tp.indexOf(last.toLowerCase());
//如果返回的结果大于或等于0，说明包含允许上传的文件类型
if(rs>=0){
    return true;
}else{
    return false;
}

}, "格式错误，请上传后缀为jpg,png,gif的图片文件");

jQuery.validator.addMethod("isExcel", function(value, element) {
if(value == '') {
    return true;
}

//为了避免转义反斜杠出问题，这里将对其进行转换
var re = /(\\+)/g;
var filename=value.replace(re,"#");
//对路径字符串进行剪切截取
var one=filename.split("#");
//获取数组中最后一个，即文件名
var two=one[one.length-1];
//再对文件名进行截取，以取得后缀名
var three=two.split(".");
//获取截取的最后一个字符串，即为后缀名
var last=three[three.length-1];

//添加需要判断的后缀名类型
var tp ="xls,xlsx";
//返回符合条件的后缀名在字符串中的位置
var rs=tp.indexOf(last.toLowerCase());
//如果返回的结果大于或等于0，说明包含允许上传的文件类型
if(rs>=0){
    return true;
}else{
    return false;
}
}, "格式错误，请上传后缀为xls,xlsx的Excel文件");
jQuery.validator.addMethod("isRequired", function(value, element) {
if(value == '') {
    return false;
}
return true;
}, "必填字段");

//验证qq
jQuery.validator.addMethod("isqq", function(value, element) {
if(this.optional(element) || /^([1-9]{1})([0-9]{4,13})$/.test(value)) {
    return true;
}else {
    return false;
}
}, "QQ格式不对，请输入5-14的数字");
//验证weixin
jQuery.validator.addMethod("isweixin", function(value, element) {
if(this.optional(element) || /^([a-zA-Z]{1})([0-9A-Za-z_-]{5,19})$/.test(value)) {
    return true;
}else {
    return false;
}
}, "微信格式不对，请输入6-20位字母数字下划线和减号");
jQuery.validator.addMethod("isDate", function(value, element){
	var ereg = /^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;
	var r = value.match(ereg);
	if (r == null) {
	return false;
	}
	var d = new Date(r[1], r[3] - 1, r[5]);
	var result = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[5]);
	return this.optional(element) || (result);
}, "请输入正确的日期");

jQuery(document).ready(function() {     
	  App.init();
});