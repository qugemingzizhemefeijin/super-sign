require "spaceship"
require "mysql2"
require 'pathname'
require Pathname.new(File.dirname(__FILE__)).realpath.to_s + '/mysqlConfig'

# certificate_pem key_pem 所使用的的p12文件 必须和 mobileprovision的cer签名文件一致
# 否则重新签名后也无法使用

certId = ARGV[0].to_s;
udid = ARGV[1].to_s;
rootPath = ARGV[2].to_s;#根目录地址
outFile = ARGV[3].to_s; #输出ipa地址

begin
	client = Mysql2::Client.new(
		:host     => MysqlConfig::HOST,     # 主机
		:username => MysqlConfig::USER,      # 用户名
		:password => MysqlConfig::PASSWORD,    # 密码
		:database => MysqlConfig::DBNAME,      # 数据库
		:encoding => MysqlConfig::CHARSET      # 编码
	)
	
	# 寻找对应的密钥信息
	results = client.query("SELECT developer_id,public_pem_path,private_pem_path FROM t_developer_cer WHERE id='#{certId}' limit 1")
	if !results.any?
		raise "密钥信息: #{certId} 不存在"
	end
	
	certificateObj = results.first
	
	developerId = certificateObj['developer_id']
	publicPemPath = rootPath + certificateObj['public_pem_path']
	privatePemPath = rootPath + certificateObj['private_pem_path']
	
	# 寻找开发者信息
	results = client.query("SELECT app_id,app_info_id FROM t_developer WHERE id='#{developerId}' limit 1")
	if !results.any?
		raise "开发者信息: #{developerId} 不存在"
	end
	
	certificateObj = results.first
	
	appId = certificateObj['app_id']
	appInfoId = certificateObj['app_info_id']
	
	# 寻找IPA源
	results = client.query("SELECT bundle_id,path,resign_mp_path FROM t_app_info WHERE id='#{appInfoId}' limit 1")
	if !results.any?
		raise "IPA信息: #{appInfoId} 不存在"
	end
	
	certificateObj = results.first
	
	bundleId = certificateObj['bundle_id']
	inFile = rootPath + certificateObj['path']
	mpPath = rootPath + certificateObj['resign_mp_path']
	
	# 删除
	system "rm -f #{outFile}"
	
	puts "/usr/local/bin/isign -c #{publicPemPath} -k #{privatePemPath} -p #{mpPath} -o #{outFile} #{inFile}"
	system "/usr/local/bin/isign -c #{publicPemPath} -k #{privatePemPath} -p #{mpPath} -o #{outFile} #{inFile}"
rescue Exception  => e
	puts "Trace message: #{e}"
else
	puts "Success message: 重签名成功"
ensure
	# 断开与服务器的连接
	client.close if client
end