//app echarts functions

;(function($){
    $.extend($,{
        /*
        *GetDate or Days
        *version 1.1
        * fix ie bug ===> - to /
        *author linan
        *@params  null number example : GetDate() return today
        *@params number example : GetDate('-3') return array [3daysbefore,today]
        *@params stringdate example : GetDate('2012-12-23') or getDate('2012.12.23') return number, days from arg0 to today
        *@params two stringdate example : GetDate('2012-12-1','2012-12-23') return number, days from arg0 to arg1
        *@params two example : $.GetDate('2012-02-23',-30) return string "2012-01-25"
        *@params two example : $.GetDate(1, true)  today is 2014-04-15 return string "2014-04-16"
        */
        GetDate : function(d1,d2){
            var len = arguments.length;
            var reg = /\d{4}([-./])\d{1,2}\1\d{1,2}/;
            var dates = [];
            var day,number;
            var today = getToday();
            switch(len){
            case 0 :
                return getToday();
                break;
            case 1:
                if(reg.test(arguments[0])){
                    return getDays(arguments[0],getToday());
                }else{
                    day = buildDate(arguments[0],today);
                    dates.push(day);
                    dates.push(getToday());
                    return dates.sort();
                }
                break;
            case 2 :
                // add by chenjincai
                // 用户群，观察时间用于获取今天之后的时间使用
                if (arguments[1] === false) {
                  return arguments.callee(arguments[0]);
                } else if (arguments[1] === true) {
                  dates.push(today);
                  dates.push(buildDateExactly(arguments[0],today));
                  return dates;
                }


                if(reg.test(arguments[0]) && reg.test(arguments[1])){
                    number = getDays(arguments[0].replace(/-/g,'/'),arguments[1].replace(/-/g,'/'));
                    return number;
                }else{
                    day = buildDate(arguments[1],arguments[0]);
                    return day;
                }
                break;

            }
            function getDays(arg,day){
                var value = new Date(day) - new Date(arg.replace(/\b(\w)\b/g, '0$1'));
                if(value<0){
                    return "Wrong Date";
                }else{
                    return parseInt(value/(1000*3600*24)+1,10);
                }
            };
            function getToday(){
                var date = new Date();
                this.Day = date.getDate();
                this.Month = date.getMonth()+1;
                this.Year = date.getFullYear();
                return (this.Year.toString()+'-'+this.Month.toString()+'-'+this.Day.toString()).replace(/\b(\w)\b/g, '0$1');;
            };
            function buildDate(num,day){
                var n = num;
                if(typeof(n) == String){
                    n = parseInt(n,10);
                }
                if(n >= 0){
                    return getToday();
                }else if (n < 0) {
                    var someDay = new Date(new Date(day.replace(/\-/g,'/'))-0+(n+1)*86400000);

                    someDay = someDay.getFullYear() + "-" + (someDay.getMonth()+1) + "-" + someDay.getDate();
                    return someDay.replace(/\b(\w)\b/g, '0$1');

                }
            }
            // 允许num大于0，num 为 0 是当天，1是明天，-1是昨天
            function buildDateExactly(num, day) {
              var n = num;
              if(typeof(n) == String){
                  n = parseInt(n,10);
              }

              var someDay = new Date(new Date(day.replace(/\-/g,'/')) - 0 + n * 86400000);

              someDay = someDay.getFullYear() + "-" + (someDay.getMonth()+1) + "-" + someDay.getDate();
              return someDay.replace(/\b(\w)\b/g, '0$1');


            }
        },
        /*
        *replace Date '-' to '.'
        *@param date example :$.replaceDate('2012-01-01')
        *return 2012.01.01
        */
        replaceDate : function(mydate){
            return mydate.replace(/-/g,'.');
        }
    })
})(jQuery);

/*
*set datepicker default settings
*/
;(function($){
    $.datepicker.regional['zh-CN'] = {
      closeText: '关闭',
      //prevText: '&#x3c;',
      //nextText: '&#x3e;',
      prevText: '',
      nextText: '',
      currentText: '',
      monthNames: ['1','2','3','4','5','6','7','8','9','10','11','12'],
      monthNamesShort: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
      dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
      dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
      dayNamesMin: ['日','一','二','三','四','五','六'],
      dateFormat: 'yy-mm-dd',
      firstDay: 0,
      isRTL: false,
      showMonthAfterYear: true,
      yearSuffix: ' .'};
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
})(jQuery);

/*
* init global variable
*/
var ZXRTB = {};
ZXRTB.prototype = window.ZXRTB;
ZXRTB.Storage = {};
ZXRTB.Storage.charts = [];
ZXRTB.Storage.compareChart = {};
window.ZXRTB.plugin = {};
window.global = {} || "";
window.global.pickedStartDay = $('.dateselect .start').eq(0).text().replace(/\./g,'-');
window.global.pickedEndDay = $('.dateselect .end').eq(0).text().replace(/\./g,'-');
window.global.pickedDays = $.GetDate(global.pickedStartDay,global.pickedEndDay);
window.global.filter = {};
window.global.filter.version = '';
window.global.filter.channel = '';
window.global.filter.segment ='',
window.global.action_stats = ZXRTB.actionStats = ZXRTB.ACTIONSTATS = $('#action_stats').val();
ZXRTB.PAGELIST = $('#priority_action_stats').val() || $('#action_stats').val();
window.global.time_unit = '';
// for components registration, { 'group_id:component_id' : component_refer}
window.global.components = {};

/*
 * local auto save users date select;
 * expend: $.GetDate method
 * 2013-06-05 add page route list
 * add expired time
 * 2013-07-26 Add on/off
 */
;(function($,win,doc,ZXRTB,undefined){
    var BLACKLIST = ['index'];
    ZXRTB = win.ZXRTB || {};
    var expire = new Date().getDate();
    var val = ZXRTB.ACTIONSTATS;
    ZXRTB.plugin.userCustomDate = {
        localCache : 'true',//localStorage.setItem('localCache',''),
        setDate: function(array,scope){
            // if page in blacklist do not cache
            var date = {};
            var flag = $.inArray(val,BLACKLIST);
            if(flag == -1 && array.constructor === Array && array.length == 2){
                array.sort();
                localStorage.setItem(scope,JSON.stringify(array));
                localStorage.setItem('localCacheDateExpireTime_'+scope,new Date().getDate());
            }
        },
        //param dateTyte is required
        getDate: function(n,dateType){
            var days = n || -7;
            if(dateType == undefined){
                return $.GetDate(days);
            };
            if(localStorage.getItem(dateType) != undefined){
                var array = new Array(localStorage[dateType]);
                return JSON.parse(array);
            }else{
                return $.GetDate(days);
            }
        }
    };
    function _setDate(dateType){
        var date = ZXRTB.plugin.userCustomDate.getDate(undefined,dateType);
        var st = $('.datepickerPanel').find('.start');
        var end = $('.datepickerPanel').find('.end');
        st.text($.replaceDate(date[0]));
        end.text($.replaceDate(date[1]));

        //reset global date
        win.global.pickedStartDay = date[0];
        win.global.pickedEndDay = date[1];
        win.global.pickedDays = $.GetDate(date[0],date[1]);
    };

    function defaultLocalDate(){
        var flag = $.inArray(val,BLACKLIST);
        if(flag == -1 && expire == localStorage['localCacheDateExpireTime_localCacheDate'] && ZXRTB.plugin.userCustomDate.localCache){
            _setDate('localCacheDate');
        }
    };

    function natureLocalDate(){
        var NATURELIST = ['funnels'];
        var flag = $.inArray(val,NATURELIST);
        if(flag > -1 && expire == localStorage['localCacheDateExpireTime_natureCacheDate'] && ZXRTB.plugin.userCustomDate.localCache){
            if(localStorage['natureCacheDate']){
                _setDate('natureCacheDate');
                var timeUnit = {
                    1: 'daily',
                    7: 'weekly',
                    28: 'monthly',
                    29: 'monthly',
                    30: 'monthly',
                    31: 'monthly'
                };
                $('.select_value').attr('timeunit',timeUnit[ZXRTB.Agent.getDate().counts || win.global.pickedDays]);
            }
        }
    };

    defaultLocalDate();
    natureLocalDate();
})(jQuery,window,document,ZXRTB);

;(function($,ZXRTB,undefined){
    if(ZXRTB != undefined){
    	ZXRTB.Agent = ZXRTB.Agent || {};
        function Agent(){
            //actionStats
            var as = $('#action_stats');
            if(as != undefined && as.length > 0){
                this.actionStats =  $('#action_stats').val();
            };
            //userDate
            this.getDate = function() {
                if ($('.dateselect').length > 1) {
                    var result = {};

                    result.date_count = $('.dateselect').length;

                    $('.dateselect').each(function(i, el) {
                        var startDate = $('.start', el).text().replace(/\./g,'-');
                        var endDate = $('.end', el).text().replace(/\./g,'-');
                        startDate == '' ? startDate = $.GetDate() : startDate = startDate;
                        endDate == '' ? endDate = $.GetDate() : endDate = endDate;
                        var c = $.GetDate(startDate,endDate);

                        result[$(el).attr('id')] = {
                            start_date: startDate,
                            end_date: endDate,
                            counts: c
                        };
                    });

                    return result;

                } else if ($('.dateselect').length > 0) {
                    var startDate = $('.dateselect .start').text().replace(/\./g,'-');
                    var endDate = $('.dateselect .end').text().replace(/\./g,'-');
                    startDate == '' ? startDate = $.GetDate() : startDate = startDate;
                    endDate == '' ? endDate = $.GetDate() : endDate = endDate;
                    var c = $.GetDate(startDate,endDate);

                    return {
                        start_date: startDate,
                        end_date: endDate,
                        counts: c
                    };
                } else {
                    return '';
                }

            };
            //
            this.getFilters = function(){
                var v = [], c = [],s = [];
                if($('#filter-version').data('checked') != undefined){
                    v.push($('#filter-version').data('checked').check == true ? $('#filter-version').data('checked').id : '');
                }else{
                    v.push('');
                }

                if($('#filter-channel').data('checked') != undefined){
                    c.push($('#filter-channel').data('checked').check == true ? $('#filter-channel').data('checked').id : '');
                }else{
                    c.push('');
                }

                if($('#filter-segment').data('checked') != undefined){
                    s.push($('#filter-segment').data('checked').check == true ? $('#filter-segment').data('checked').id : '');
                }else{
                    s.push('');
                }
                return {
                    versions: v,
                    channels: c,
                    segments: s
                }
            };

            this.getTimeunit = function(){
                var unit = $('.js-um-timeUnit').timeUnit('getUnit');
                if(unit.length > 0){
                    return unit;
                }else{
                    return 'daily';
                }
            };
            this.getTimeunitArray = function(){
                var unit = [];
                $('.js-um-timeUnit').each(function(){
                    unit.push($(this).timeUnit('getUnit'));
                });
                return unit;
            };
            this.buildParams = function(params){
                params = params || {};

                var date = (typeof(this.getDate().date_count) === 'undefined') ? this.getDate() : undefined; // if has multi date, set undefined
                var filters = this.getFilters();
                var timeunit = this.getTimeunit();
                var stats = ZXRTB.ACTIONSTATS;
                var p = $.extend({},date,filters,{time_unit: timeunit,stats: stats},params);
                delete p.counts;
                return p;
            }

        }
        ZXRTB.Agent = new Agent();
    }else{
        console.log('E: UMENG(OBJECT) NOT DEFINED!');
    }
})(jQuery,window.ZXRTB);

//jquery barlines
(function($, undefined) {
	;(function($,ZXRTB,undefined){
	    if(ZXRTB != undefined){
	    	ZXRTB.Agent = ZXRTB.Agent || {};
	        function Agent(){
	            //actionStats
	            var as = $('#action_stats');
	            if(as != undefined && as.length > 0){
	                this.actionStats =  $('#action_stats').val();
	            };
	            //userDate
	            this.getDate = function() {
	                if ($('.dateselect').length > 1) {
	                    var result = {};

	                    result.date_count = $('.dateselect').length;

	                    $('.dateselect').each(function(i, el) {
	                        var startDate = $('.start', el).text().replace(/\./g,'-');
	                        var endDate = $('.end', el).text().replace(/\./g,'-');
	                        startDate == '' ? startDate = $.GetDate() : startDate = startDate;
	                        endDate == '' ? endDate = $.GetDate() : endDate = endDate;
	                        var c = $.GetDate(startDate,endDate);

	                        result[$(el).attr('id')] = {
	                            start_date: startDate,
	                            end_date: endDate,
	                            counts: c
	                        };
	                    });

	                    return result;

	                } else if ($('.dateselect').length > 0) {
	                    var startDate = $('.dateselect .start').text().replace(/\./g,'-');
	                    var endDate = $('.dateselect .end').text().replace(/\./g,'-');
	                    startDate == '' ? startDate = $.GetDate() : startDate = startDate;
	                    endDate == '' ? endDate = $.GetDate() : endDate = endDate;
	                    var c = $.GetDate(startDate,endDate);

	                    return {
	                        start_date: startDate,
	                        end_date: endDate,
	                        counts: c
	                    };
	                } else {
	                    return '';
	                }

	            };
	            //
	            this.getFilters = function(){
	                var v = [], c = [],s = [];
	                if($('#filter-version').data('checked') != undefined){
	                    v.push($('#filter-version').data('checked').check == true ? $('#filter-version').data('checked').id : '');
	                }else{
	                    v.push('');
	                }

	                if($('#filter-channel').data('checked') != undefined){
	                    c.push($('#filter-channel').data('checked').check == true ? $('#filter-channel').data('checked').id : '');
	                }else{
	                    c.push('');
	                }

	                if($('#filter-segment').data('checked') != undefined){
	                    s.push($('#filter-segment').data('checked').check == true ? $('#filter-segment').data('checked').id : '');
	                }else{
	                    s.push('');
	                }
	                return {
	                    versions: v,
	                    channels: c,
	                    segments: s
	                }
	            };

	            this.getTimeunit = function(){
	                var unit = $('.js-um-timeUnit').timeUnit('getUnit');
	                if(unit.length > 0){
	                    return unit;
	                }else{
	                    return 'daily';
	                }
	            };
	            this.getTimeunitArray = function(){
	                var unit = [];
	                $('.js-um-timeUnit').each(function(){
	                    unit.push($(this).timeUnit('getUnit'));
	                });
	                return unit;
	            };
	            this.buildParams = function(params){
	                params = params || {};

	                var date = (typeof(this.getDate().date_count) === 'undefined') ? this.getDate() : undefined; // if has multi date, set undefined
	                var filters = this.getFilters();
	                var timeunit = this.getTimeunit();
	                var stats = ZXRTB.ACTIONSTATS;
	                var p = $.extend({},date,filters,{time_unit: timeunit,stats: stats},params);
	                delete p.counts;
	                return p;
	            }

	        }
	        ZXRTB.Agent = new Agent();
	    }else{
	        console.log('E: UMENG(OBJECT) NOT DEFINED!');
	    }
	})(jQuery,window.ZXRTB);
	/*
	 * ProSelect 1.0
	 * Author: linan
	 * Depends: jQuery, modules
	 *
	 * Modify:
	 *   2014.xx.xx:
	 *     Support minDate & maxDate - Chenjincai
	 *       - Add minDate/maxDate
	 */

	;(function($) {

	  var datePickerArray = [] /*= { startDay: '', endDay: '' }*/,date = new Date();

	  window.thisYear = date.getFullYear();

	  $.fn.ProSelect = function(options) {
	    if (this.length > 0) {
	      var $this = this;

	      var defaults = {
	        show: false,
	        x: $this,
	        targettext: $this.text(),
	        muti: false,
	        temp: '<div id ="proTemp" class="proTemp">test</div>',
	        tempid: 'proTemp',
	        clazz : 'proTemp',
	        inputname: ['过去60天', '过去30天', '过去7天', '今日'],
	        inputval: ['-60','-30','-7','-1'],
	        custom: true,
	        callback: '',
	        minDate: null, // add by chenjincai 我的产品-->应用趋势选择控件有用，禁止选择8.1, 之前的日期，传递的是Date Object, 然后传给datepicker使用
	        maxDate: +0 // add by chenjincai 仅支持数字，用于设置最大可以选择的日期
	      };

	      if (options) {
	        var set = $.extend(false, {}, defaults, options);
	      }
	      
	      if(typeof datePickerArray[set.clazz] == 'undefined') datePickerArray[set.clazz] = { startDay: '', endDay: '' };
	      datePickerArray[set.clazz].startDay = $('.start', $this).text().replace(/\./g, '-');
	      datePickerArray[set.clazz].endDay = $('.end', $this).text().replace(/\./g, '-');

	      $this.set = set;

	      init($this, set);

	      $.extend($.fn.ProSelect.prototype, {
	        alert: function(){ console.log(set) },
	        buildmodule: function() {
	          if (set.temp) {
	            if (set.inputname.length == set.inputval.length) {
	              var _temp = '<div id ="' + set.tempid + '" class="'+set.clazz+'"><ul>';

	              for (i = 0; i < set.inputname.length; i++) {
	                var val  = set.inputval[i] == '' ? 'custom' :  set.inputval[i];
	                _temp += '<li set-value="'+ val +'">' + set.inputname[i] + '</li>'
	              }

	              _temp += '</ul></div>';
	            }
	          }
	          return _temp;
	        },
	        bindclick: function($this) {
	          var $t = $this;

	          $t.next().find('li').each(function() {
	            $(this).on('click', function() {

	              if ($(this).attr('set-value')!='') {
	                var n = $(this).attr('set-value');
	                var days = [];
	                if(n.indexOf('#')>-1) {
	                	n = n.replace('#','');
	                	if ($t.set.maxDate !== 0) {
	                        n = parseInt(n) + $t.set.maxDate;
	                    }
	                	
	                	if ($t.set.minDate !== null && $t.set.minDate > new Date($.GetDate(parseInt(n))[0])) {
	                    	days.push(($t.set.minDate.getFullYear() + '-' + ($t.set.minDate.getMonth()+1) + '-' + $t.set.minDate.getDate()).replace(/\b(\w)\b/g, '0$1'))
	                    } else {
	                        days.push($.GetDate(parseInt(n))[0]);
	                    }

	                    days.push($.GetDate(parseInt(n))[0]);
	                } else {
	                	if ($t.set.maxDate !== 0) {
	                        n = parseInt(n) + $t.set.maxDate;
	                    }

	                    if ($t.set.minDate !== null && $t.set.minDate > new Date($.GetDate(parseInt(n))[0])) {
	                    	days.push(($t.set.minDate.getFullYear() + '-' + ($t.set.minDate.getMonth()+1) + '-' + $t.set.minDate.getDate()).replace(/\b(\w)\b/g, '0$1'))
	                    } else {
	                        days.push($.GetDate(parseInt(n))[0]);
	                    }

	                    days.push($.GetDate(parseInt($t.set.maxDate), true)[1]);
	                }
	                //var days = $.GetDate(parseInt(n));
	                $t.html('<span class="start">' + $.replaceDate(days[0]) + '</span>' + ' - ' + '<span class="end">' + $.replaceDate(days[1]) + '</span><b class="icon pulldown"></b>');

	                $t.next().hide();

	                window.global.pickedStartDay = days[0];
	                window.global.pickedEndDay = days[1];
	                window.global.pickedDays = Math.abs(n);

	                var label = window.global.pickedDays + '天';
	                $t.set.callback(days,window.global.pickedDays,$(this));
	              } else {
	                $('#datepickerStart_'+set.clazz,$t.next()).show();
	              }

	              return false;
	            });
	          });
	        },
	        datepickerInit: function($this,op) {
	        	var $t = $this;
	          $("#datepickerStart_"+op.clazz,$t.next()).datepicker({
	            dateFormat: "yy-mm-dd",
	            inline: true,
	            defaultDate: datePickerArray[op.clazz].startDay,
	            minDate: $this.set.minDate,
	            maxDate: $this.set.maxDate,
	            yearRange: '2000:' + window.thisYear,
	            onSelect: function(dateText, inst, e) {
	              $("#datepickerEnd_"+op.clazz,$t.next()).datepicker('option', 'minDate', dateText);
	              datePickerArray[op.clazz].startDay = dateText;
	              return false;
	            }
	          });

	          $("#datepickerEnd_"+op.clazz,$t.next()).datepicker({
	            dateFormat: "yy-mm-dd",
	            inline: true,
	            defaultDate: datePickerArray[op.clazz].endDay,
	            minDate: $this.set.minDate,
	            maxDate: $this.set.maxDate,
	            yearRange: '2000:' + window.thisYear,
	            onSelect: function(dateText, inst, e) {
	              $("#datepickerStart_"+op.clazz,$t.next()).datepicker('option', 'maxDate', dateText);
	              datePickerArray[op.clazz].endDay = dateText;
	              return false;
	            }
	          });
	        }
	      });

	      _setDefault(set,$this);

	      return $this;
	    }
	  };

	  var init = function($this,op) {
	    $this.bind('click', function() {
	      if (!$this.next().is(':visible')) {
	        if (!$this.next().is('#' + op.tempid)) {
	          $this.after($this.ProSelect.prototype.buildmodule);
	          _custom($this,op);
	          $this.ProSelect.prototype.bindclick($this);
	        } else {
	        	$this.next().show();
	        }
	      } else {
	    	  $this.next().hide();
	      }

	      op.show = true;

	      $('.ui-datepicker-calendar').on('click', function(e) {
	        e.stopPropagation();
	      });

	      $('.ui-datepicker-header').on('click', function(e) {
	        e.stopPropagation();
	      });

	      return false;
	    });
	  };

	  function _custom($this, op) {
		  var $t = $this;
	    if (op.custom) {
	      var tmp = '<a class="customhref" href="#">自选</a><div id="datePickerPanel" style="display:none;"><div id="datepickerStart_'+op.clazz+'"></div><div id="datepickerEnd_'+op.clazz+'"></div><input type="button" value="确定" class="custombtn" style="clear:both;display:block"/></div>';
	      $t.next().append(tmp);

	      $('.customhref',$t.next()).bind('click', function() {
	        $(this).next().show();
	        $t.ProSelect.prototype.datepickerInit($t,op);
	        return false;
	      });

	      $('.custombtn',$t.next()).bind('click', function() {
	    	  $t.next().hide();
	        $('#datePickerPanel',$t.next()).hide();
	        op.show = false;

	        window.global.pickedStartDay = datePickerArray[op.clazz].startDay;
	        window.global.pickedEndDay = datePickerArray[op.clazz].endDay;
	        window.global.pickedDays = $.GetDate(window.global.pickedStartDay,window.global.pickedEndDay);

	        var label = window.global.pickedDays + '天';

	        $t.find('.start').html( $.replaceDate(datePickerArray[op.clazz].startDay) );
	        $t.find('.end').html( $.replaceDate(datePickerArray[op.clazz].endDay) );

	        op.callback([datePickerArray[op.clazz].startDay, datePickerArray[op.clazz].endDay], window.global.pickedDays);

	        return false;
	      });
	    }
	  };

	  //fix ui click bubbling page
	  var _setDefault = function(op , $this) {
	    $(document).bind('click', function(e) {
	      if ($(e.target).parents('table.ui-datepicker-calendar').length > 0 || $(e.target).parents('#datePickerPanel').length > 0 || $(e.target).parents('.ui-datepicker-header').length > 0) {
	        return false;
	      } else {
	    	$this.next().hide();
	        $('#datePickerPanel',$this.next()).hide();
	        op.show = false;
	      }
	    });
	  };
	})(jQuery);
	
	/*
	* Time Unit Widget
	*
	* Author: wangfang@umeng.com
	* Date: 2013.12.09
	* Dependencies:
	*/
	;(function($) {
	    $.widget("bluebee.timeUnit", {
	        options: {
	            onClickItem: function() {}
	        },
	        _create: function() {
	            var self  = this,
	                o     = self.options,
	                el    = self.element,
	                elID  = el.get(0).id,
	                items = el.find('li');
	            // init style
	            $.each(items, function(i, item) {
	                var $item = $(item);
	                if($item.attr('data-on') === 'true') $item.addClass('on');
	                if($item.attr('data-enable') === 'true') self._setItemEnable($item);
	                else if($item.attr('data-enable') === 'false') self._setItemDisable($item);
	            });
	            // set on item
	            $.subscribe("timeunit.on", function(event, unit, callback) {
	                items.removeClass('on').filter('[data-unit="' + unit + '"]').addClass('on');
	                if (typeof callback == 'function') {
	                    try {
	                        callback();
	                    } catch (e) {}
	                }
	            });
	            // set enable items
	            $.subscribe("timeunit.disable", function(event, units, callback) {
	                $.each(items, function(i, item) {
	                    var $item = $(item);
	                    (units.indexOf($item.attr('data-unit')) != -1) ? self._setItemDisable($item) : self._setItemEnable($item);
	                });
	                if (typeof callback == 'function') {
	                    try {
	                        callback();
	                    } catch (e) {}
	                }
	            });
	        },
	        _setItemEnable: function($item) {
	            var self = this,
	                o    = self.options;  
	            $item
	                .removeClass('off')
	                .attr('data-enable', 'true')
	                .off().on('click.timeunit', function(e) {
	                    if(!$item.hasClass('on')) {
	                        $item.addClass('on').siblings('li[data-enable="true"]').removeClass('on');
	                        o.onClickItem($item.attr('data-unit'));
	                    }
	                });
	        },
	        _setItemDisable: function($item) {
	            var self  = this,
	                o     = self.options;
	            if ($item.hasClass('on')) {
	                $item.off().removeClass('on').addClass('off').attr('data-enable', 'false');
	                if ($item.next().length !== 0) {
	                    self._setItemEnable($item.next());
	                } else {
	                    var $first = $item.siblings('li[data-enable="true"]:eq(0)');
	                    if ($first.length !== 0) {
	                        $first.addClass('on');
	                        self._setItemEnable($first);
	                    }
	                }
	            } else {
	                $item.off().addClass('off').attr('data-enable', 'false');
	            }
	        },
	        getUnit: function() {
	            return this.element.find('li.on').data('unit');
	        },
	        set: function(opts) {
	            var self  = this,
	                el    = self.element;
	                items = el.find('li');
	            $.each(items, function(i, item) {
	                var $item = $(item).removeClass('on');
	                if (opts.disable.indexOf($item.data('unit')) != -1 ) {
	                    self._setItemDisable($item);
	                } else {
	                    self._setItemEnable($item);
	                    if($item.data('unit') === opts.on) $item.addClass('on');
	                }
	            });
	        },
	        onClickItem: function(callback) {
	            var self  = this,
	                el    = self.element;
	                items = el.find('li');
	            $.each(items, function(i, item) {
	                var $item = $(item);
	                    self.options.onClickItem = callback;
	                if($item.attr('data-enable') === 'true') {
	                    $item.off().on('click.timeunit', function(e) {
	                        $item.addClass('on').siblings().removeClass('on');
	                        self.options.onClickItem($item.attr('data-unit'));
	                    });
	                }
	            });
	        }
	    });
	    // auto init
	    $(function () {
	        $('.js-um-timeUnit').timeUnit();
	    });
	})(jQuery);
	
	$.widget('bluebee.renderBarLineChart', {
		options : {
			title : '柱形+折线图',
			themes : 'zhixing',
			url: '/reports/load_chart_data',
			method : 'get',
			params: {},
			chartOptions : {},
			callback : function(){
				
			}
		},
		_init : function(){
			//loading
			this.element.html('<div class="spiner-example"><div class="sk-spinner sk-spinner-wave"><div class="sk-rect1"></div> <div class="sk-rect2"></div> <div class="sk-rect3"></div> <div class="sk-rect4"></div> <div class="sk-rect5"></div></div></div>');
            this._getSeries();
		},
		_readCache: function(chartDNA) {
            for (i in ZXRTB.Storage.charts) {
                if (ZXRTB.Storage.charts[i].chartID == chartDNA) {
                    return ZXRTB.Storage.charts[i].chartBody;
                }
            }
            return undefined;
        },
        _writeCache: function(options) {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            ZXRTB.Storage.charts.push({
                chartID: chartDNA,
                chartBody: options,
                callback: this.options.callback
            })
        },
      //Get series by ajax
        _getSeries: function() {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            var that = this;
            var data = this._readCache(chartDNA);
            //update callback function
            function docallback() {
                that.element.data('currentChart', that.chartDNA);
                that.options.callback(that.element);
            }
            if (data != undefined) {
                var myChart = echarts.init(this.element.get(0) , this.options.themes);
                //使用刚指定的配置项和数据显示图表。
                myChart.setOption(data);
                docallback();
                return;
            }
            //ajax
            $.ajax({
                type: that.options.method,
                url: that.options.url,
                data: that.options.params,
                success: function(results) {
                    if (results.result != 'success') {
                        that.errorResult(results, that.options, that.element);
                    } else {
                        that._renderSeries(results , docallback);
                    }
                }
            });
        },
        _renderSeries : function(results , docallback){
        	var yAxis = [] ,
        	series = [],
        	legend = [],
        	yresult = results.yaxis,
        	datas = results.datas,
        	title = (typeof results.title == 'undefined' ? this.options.title : results.title);
        	
        	//初始化yaxis
        	$.each(yresult, function(i, y) {
        		yAxis[i] = {
	                type: y.type,
	                name: y.name,
	                axisLabel: {
	                    formatter: '{'+y.type+'} '+y.unit
	                }
	            };
        		if(typeof y.min != 'undefined') yAxis[i].min = y.min;
        		if(typeof y.max != 'undefined') yAxis[i].max = y.max;
        		if(typeof y.interval != 'undefined') yAxis[i].interval = y.interval;
        	});
        	//初始化series
        	$.each(datas, function(i, d) {
        		series[i] = {
    	            name:d.name,
    	            type:d.type,
    	            data:d.data
    	        };
        		legend[i] = d.name;
        		if(typeof d.yaxisIndex != 'undefined') series[i].yAxisIndex = d.yaxisIndex;
        	});
        	
        	var chartOptions = {
    			/*title : {
    		        text: results.title,
    		        subtext: results.subtext
    		    },*/
		        tooltip: {
		            trigger: 'axis'
		        },
		        toolbox: {
		            feature: {
		                dataView: {show: true, readOnly: false},
		                magicType: {show: true, type: ['line', 'bar']},
		                restore: {show: true},
		                saveAsImage: {show: true}
		            }
		        },
		        legend: {
		            data:legend
		        },
		        xAxis: results.xaxis,
		        yAxis: yAxis,
		        series: series
		    };

        	var myChart = echarts.init(this.element.get(0) , this.options.themes);
            //使用刚指定的配置项和数据显示图表。
            myChart.setOption(chartOptions);
            docallback();
            
            this._writeCache(chartOptions);
        },
        errorResult: function(results, options, elem) {
            this.element.html('<div class="spiner-example"><div class="tip-noResult">'+results.msg+'</div></div>');
            try {
                //this.options.callback(this.element);
            } catch (e) {}
        },
        destroy: function() {
            console.log('destroy!');
            $.Widget.prototype.destroy.call(this);
        }
	});
	
	//折线图
	$.widget('bluebee.renderLineChart', {
		options : {
			title : '折线图',
			themes : 'zhixing',
			url: '/reports/load_chart_data',
			method : 'get',
			params: {},
			chartOptions : {},
			callback : function(){
				
			}
		},
		_init : function(){
			//loading
			this.element.html('<div class="spiner-example"><div class="sk-spinner sk-spinner-wave"><div class="sk-rect1"></div> <div class="sk-rect2"></div> <div class="sk-rect3"></div> <div class="sk-rect4"></div> <div class="sk-rect5"></div></div></div>');
            this._getSeries();
		},
		_readCache: function(chartDNA) {
            for (i in ZXRTB.Storage.charts) {
                if (ZXRTB.Storage.charts[i].chartID == chartDNA) {
                    return ZXRTB.Storage.charts[i].chartBody;
                }
            }
            return undefined;
        },
        _writeCache: function(options) {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            ZXRTB.Storage.charts.push({
                chartID: chartDNA,
                chartBody: options,
                callback: this.options.callback
            })
        },
      //Get series by ajax
        _getSeries: function() {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            var that = this;
            var data = this._readCache(chartDNA);
            //update callback function
            function docallback() {
                that.element.data('currentChart', that.chartDNA);
                that.options.callback(that.element);
            }
            if (data != undefined) {
                var myChart = echarts.init(this.element.get(0) , this.options.themes);
                //使用刚指定的配置项和数据显示图表。
                myChart.setOption(data);
                docallback();
                return;
            }
            //ajax
            $.ajax({
                type: that.options.method,
                url: that.options.url,
                data: that.options.params,
                success: function(results) {
                    if (results.result != 'success') {
                        that.errorResult(results, that.options, that.element);
                    } else {
                        that._renderSeries(results , docallback);
                    }
                }
            });
        },
        _renderSeries : function(results , docallback){
        	var yAxis = [] ,
        	series = [],
        	legend = [],
        	yresult = results.yaxis,
        	datas = results.datas,
        	tooltip = {
       			trigger: 'axis'
        	};
        	title = (typeof results.title == 'undefined' ? this.options.title : results.title);
        	
        	//初始化yaxis
        	yAxis = {
    			type: yresult.type,
                name: yresult.name,
                axisLabel: {
                    formatter: '{'+yresult.type+'} '+yresult.unit
                }
        	}
        	if(typeof yresult.min != 'undefined') yAxis.min = yresult.min;
    		if(typeof yresult.max != 'undefined') yAxis.max = yresult.max;
    		if(typeof yresult.interval != 'undefined') yAxis.interval = yresult.interval;
        	//初始化series
    		var formatter = '';
        	$.each(datas, function(i, d) {
        		series[i] = {
    	            name:d.name,
    	            type:d.type,
    	            data:d.data
    	        };
        		legend[i] = d.name;
        		formatter += '{a'+i+'}: {c'+i+'} '+yresult.unit+'<br />';
        		if(typeof d.yaxisIndex != 'undefined') series[i].yAxisIndex = d.yaxisIndex;
        	});
        	
        	//设置tooltip提示显示格式
        	if(formatter != '') {
        		tooltip.formatter = formatter;
        	}
        	
        	var chartOptions = {
    			/*title : {
    		        text: results.title,
    		        subtext: results.subtext
    		    },*/
		        tooltip: tooltip,
		        toolbox: {
		            feature: {
		                dataView: {show: true, readOnly: false},
		                magicType: {show: true, type: ['line']},
		                restore: {show: true},
		                saveAsImage: {show: true}
		            }
		        },
		        legend: {
		            data:legend
		        },
		        xAxis: results.xaxis,
		        yAxis: yAxis,
		        series: series
		    };

        	var myChart = echarts.init(this.element.get(0) , this.options.themes);
            //使用刚指定的配置项和数据显示图表。
            myChart.setOption(chartOptions);
            docallback();
            
            this._writeCache(chartOptions);
        },
        errorResult: function(results, options, elem) {
            this.element.html('<div class="spiner-example"><div class="tip-noResult">'+results.msg+'</div></div>');
            try {
                //this.options.callback(this.element);
            } catch (e) {}
        },
        destroy: function() {
            console.log('destroy!');
            $.Widget.prototype.destroy.call(this);
        }
	});
	//饼图
	$.widget('bluebee.renderPieChart', {
		options : {
			title : '饼图',
			subtitle : '',
			themes : 'zhixing',
			url: '/reports/load_chart_data',
			method : 'get',
			params: {},
			chartOptions : {},
			callback : function(){
				
			}
		},
		_init : function(){
			//loading
			this.element.html('<div class="spiner-example"><div class="sk-spinner sk-spinner-wave"><div class="sk-rect1"></div> <div class="sk-rect2"></div> <div class="sk-rect3"></div> <div class="sk-rect4"></div> <div class="sk-rect5"></div></div></div>');
            this._getSeries();
		},
		_readCache: function(chartDNA) {
            for (i in ZXRTB.Storage.charts) {
                if (ZXRTB.Storage.charts[i].chartID == chartDNA) {
                    return ZXRTB.Storage.charts[i].chartBody;
                }
            }
            return undefined;
        },
        _writeCache: function(options) {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            ZXRTB.Storage.charts.push({
                chartID: chartDNA,
                chartBody: options,
                callback: this.options.callback
            })
        },
      //Get series by ajax
        _getSeries: function() {
            var chartDNA = MD5(JSON.stringify(this.options.params) + this.options.url);
            var that = this;
            var data = this._readCache(chartDNA);
            //update callback function
            function docallback() {
                that.element.data('currentChart', that.chartDNA);
                that.options.callback(that.element);
            }
            if (data != undefined) {
                var myChart = echarts.init(this.element.get(0) , this.options.themes);
                //使用刚指定的配置项和数据显示图表。
                myChart.setOption(data);
                docallback();
                return;
            }
            //ajax
            $.ajax({
                type: that.options.method,
                url: that.options.url,
                data: that.options.params,
                success: function(results) {
                    if (results.result != 'success') {
                        that.errorResult(results, that.options, that.element);
                    } else {
                        that._renderSeries(results , docallback);
                    }
                }
            });
        },
        _renderSeries : function(results , docallback){
        	var series = [{
        		name : results.name,
        		type:'pie',
        		radius: results.type==0?'65%':['40%', '65%'],
   				data : [],
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 20,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
        	}],
        	legend = {
    			orient: 'vertical',
    	        x: 'left',
    	        data:[]
        	},
        	datas = results.datas,
        	tooltip = {
       			trigger: 'item',
       			formatter : '{a} <br/>{b}: {c}'+results.unit+' ({d}%)'
        	};
        	title = (typeof results.title == 'undefined' ? this.options.title : results.title);
        	subtitle = (typeof results.subtitle == 'undefined' ? this.options.subtitle : results.subtitle);
        	
        	//初始化series
        	$.each(datas, function(i, d) {
        		series[0].data[i] = {
    	            name:d.name,
    	            value:d.value
    	        };
        		legend[i] = d.name;
        	});
        	
        	var chartOptions = {
    			title : {
    		        text: results.title,
    		        subtext: results.subtext,
    		        x:'center'
    		    },
		        tooltip: tooltip,
		        legend: legend,
		        series : series
		    };

        	var myChart = echarts.init(this.element.get(0) , this.options.themes);
            //使用刚指定的配置项和数据显示图表。
            myChart.setOption(chartOptions);
            docallback();
            
            this._writeCache(chartOptions);
        },
        errorResult: function(results, options, elem) {
            this.element.html('<div class="spiner-example"><div class="tip-noResult">'+results.msg+'</div></div>');
            try {
                //this.options.callback(this.element);
            } catch (e) {}
        },
        destroy: function() {
            console.log('destroy!');
            $.Widget.prototype.destroy.call(this);
        }
	});
})(jQuery);