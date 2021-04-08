require "spaceship"
require 'openssl'
require "mysql2"
require 'pathname'
require Pathname.new(File.dirname(__FILE__)).realpath.to_s + '/mysqlConfig'

certId = ARGV[0].to_s;
udid = ARGV[1].to_s;
mobileprovision = ARGV[2].to_s;	#mp文件的保存路径

# 将指定的
def ad_hocCreate(bundleId, certificateId, username)
	cert = Spaceship::Portal.certificate.production.find(certificateId)
	if !cert
		raise "证书#{certificateId} 不存在"
	end
	
	#创建 ad_hoc
	Spaceship::Portal.provisioning_profile.ad_hoc.create!(bundle_id: bundleId, certificate: cert, name: username)
	sleep 1
end

begin
	# 连接mysql
	client = Mysql2::Client.new(
		:host     => MysqlConfig::HOST,     # 主机
		:username => MysqlConfig::USER,      # 用户名
		:password => MysqlConfig::PASSWORD,    # 密码
		:database => MysqlConfig::DBNAME,      # 数据库
		:encoding => MysqlConfig::CHARSET      # 编码
	)
	
	# 寻找对应的密钥信息
	results = client.query("SELECT developer_id,certificate_id FROM t_developer_cer WHERE id='#{certId}' limit 1")
	if !results.any?
		raise "密钥信息: #{certId} 不存在"
	end
	
	certificateObj = results.first
	
	developerId = certificateObj['developer_id']
	certificateId = certificateObj['certificate_id']
	
	# 寻找开发者信息
	results = client.query("SELECT app_id,app_info_id,username,password FROM t_developer WHERE id='#{developerId}' limit 1")
	if !results.any?
		raise "开发者信息: #{developerId} 不存在"
	end
	
	certificateObj = results.first
	
	appId = certificateObj['app_id']
	appInfoId = certificateObj['app_info_id']
	username = certificateObj['username']
	password = certificateObj['password']
	
	# 寻找APP信息
	results = client.query("SELECT bundle_id FROM t_app_info WHERE id='#{appInfoId}' limit 1")
	if !results.any?
		raise "APP信息: #{appInfoId} 不存在"
	end
	
	certificateObj = results.first
	
	bundleId = certificateObj['bundle_id']

	# 登录
	Spaceship::Portal.login(username, password)
	# 添加 bundleId
	app = Spaceship::Portal.app.find(bundleId)
	if !app
		raise "#{bundleId} 不存在"
	end
	
	# 获取所有证书
	certificates = Spaceship::Portal.certificate.all
	if certificates.empty?
		raise "证书为空"
	end
	
	# 如果udid不存在则添加udid
	if !Spaceship::Portal.device.find_by_udid(udid)
		Spaceship::Portal.device.create!(name:udid, udid: udid)
		
		# 更新设备数量
		deviceLength = Spaceship::Portal.device.all.length
		c_time = Time.now.strftime("%Y-%m-%d %H:%M:%S")
		
		puts "deviceLength:" + deviceLength.to_s + ",time:" + c_time
		
		client.query("update t_developer set install_limit='#{deviceLength}',virtual_limit='#{deviceLength}' where id = '#{developer_id}'")
		# 添加一条udid记录
		client.query("insert into t_udid_add_log(create_time,update_time,app_id,app_info_Id,developer_id,udid) values('#{c_time}', '#{c_time}', '#{appId}', '#{appInfoId}', '#{developerId}', '#{udid}')")
	else
		results = client.query("select id from t_udid_add_log where app_id='#{appId}' and udid='#{udid}' limit 1")
		c_time = Time.now.strftime("%Y-%m-%d %H:%M:%S")
		# 数据不存在，则条件一条记录
		if !results.any?
			client.query("insert into t_udid_add_log(create_time,update_time,app_id,app_info_Id,developer_id,udid) values('#{c_time}', '#{c_time}', '#{appId}', '#{appInfoId}', '#{developerId}', '#{udid}')")
		end
	end
	
	Spaceship.provisioning_profile.ad_hoc.all.each do |p|
		#遍历查找对应 bundleId 和 certificateId 的 profile
		p.certificates.each do |cs|
			if cs.id == certificateId && p.app.bundle_id == bundleId
				$ad_hocProfile = p
			end
		end
	end
	
	#判断如果没有找到ad_hoc
	if !defined? $ad_hocProfile
		ad_hocCreate(bundleId, certificateId, bundleId + '.' + certificateId)
		sleep 1
		$ad_hocProfile = Spaceship.provisioning_profile.ad_hoc.all.first
	end
	
	if !defined? $ad_hocProfile
		raise "ad_hoc profile 生成失败"
	end
	
	# 设备号
	devices = Spaceship.device.all
	# 根据cert 证书创建
	# 更新 ad_hoc
	$ad_hocProfile.devices = devices
	$ad_hocProfile.update!
	
	# 重新从线上获取数据
	Spaceship.provisioning_profile.ad_hoc.all.each do |p|
		if p.name == $ad_hocProfile.name
			File.write(mobileprovision, p.download)
		end
	end
rescue Exception  => e
	puts "Trace message: #{e}"
else
	puts "Success message: udid添加成功"
ensure
	# 断开与服务器的连接
	client.close if client
end
