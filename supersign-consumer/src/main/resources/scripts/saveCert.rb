require "spaceship"
require 'openssl'
require "mysql2"
require 'pathname'
require Pathname.new(File.dirname(__FILE__)).realpath.to_s + '/mysqlConfig'

# 设置 ser证书 和 上传p12文件

username = ARGV[0].to_s;
password = ARGV[1].to_s;
developerId = ARGV[2].to_s;
rootPath = ARGV[3].to_s;#根目录地址
certificateDir = ARGV[4].to_s;#相对路径
p12Pass = ARGV[5].to_s;#p12的证书密码
p12Path = ARGV[6].to_s;#请传递绝对路径

certificateId = '';
privatePemPath = '/private_key.pem';
publicPemPath = '/public_key.pem';

begin
	Spaceship::Portal.login(username, password)
	
	# 获取所有证书
	certificates = Spaceship::Portal.certificate.all
	
	if certificates.empty?
		#创建一个新证书
		csr, pkey = Spaceship::Portal.certificate.create_certificate_signing_request
		certificateObj = Spaceship::Portal.certificate.production.create!(csr: csr)
		certificateId = certificateObj.id
		cTime = certificateObj.created
		eTime = certificateObj.expires
		
		# 载入证书
		certificate = csr
		
		keyPath = rootPath + certificateDir + '/' + certificateId
		system "mkdir -p #{keyPath}"
		system "chmod 755 #{keyPath}"
		
		# 写入证书
		x509_certificate = certificateObj.download
		
		# cer证书
		File.write(keyPath + "/#{certificateId}.csr", csr.to_der)
		# 私钥
		File.write(keyPath + "/#{certificateId}.pkey", pkey.to_pem)
		
		# p12文件
		p12Key =  "/#{certificateId}.p12";
		p12Path = keyPath + p12Key
		p12Pass = ''
		
		p12 = OpenSSL::PKCS12.create('', 'production', pkey, x509_certificate)
		File.write(p12Path, p12.to_der)
		
		# x509_cer
		x509_cert_path = keyPath + "/#{certificateId}.x509_cert_path.pem"
		File.write(x509_cert_path, x509_certificate.to_pem + pkey.to_pem)
	else
		if p12Path.empty?
			raise  'p12Path 文件不存在'
		end
		
		certificateObj = Spaceship::Portal.certificate.production.all.first
		certificateId = certificateObj.id
		cTime = certificateObj.created
		eTime = certificateObj.expires
		
		keyPath = rootPath + certificateDir + '/' + certificateId
		system "mkdir -p #{keyPath}"
		system "chmod 755 #{keyPath}"
	end
	
	privatePemPath = certificateDir + '/private_key.pem';
	publicPemPath = certificateDir + '/public_key.pem';
	
	# 解析出公钥
	output =  system "openssl pkcs12 -password pass:#{p12Pass} -in #{p12Path} -out #{rootPath + publicPemPath} -clcerts -nokeys"
	if !output
		raise puts  "openssl pkcs12  -password pass:#{p12Pass} -in #{p12Path} -out #{rootPath + publicPemPath} -clcerts -nokeys  失败"
	end
	
	#解析出私钥
	output = system "openssl pkcs12  -password pass:#{p12Pass} -in #{p12Path} -out #{rootPath + privateKey} -nocerts -nodes"
	if !output
		raise puts  "openssl pkcs12  -password pass:#{p12Pass} -in #{p12Path} -out #{rootPath + privateKey} -nocerts -nodes  失败"
	end
	
	# 更新mysql
	client = Mysql2::Client.new(
		:host     => MysqlConfig::HOST,     # 主机
		:username => MysqlConfig::USER,      # 用户名
		:password => MysqlConfig::PASSWORD,    # 密码
		:database => MysqlConfig::DBNAME,      # 数据库
		:encoding => MysqlConfig::CHARSET      # 编码
	)
	
	now_time = Time.now.strftime("%Y-%m-%d %H:%M:%S")
	
	#查询 证书id是否存在
	results = client.query("SELECT id FROM t_developer_cer WHERE certificate_id= '#{certificateId}'")
	if results.any?
		#存在 更新
		client.query("update t_developer_cer set public_pem_path = '#{publicPemPath}', private_pem_path = '#{privatePemPath}', expire_time = '#{eTime}' where certificate_id = '#{certificateId}'")
	else
		#不存在保存
		client.query("insert into t_developer_cer(create_time, update_time, developer_id, certificate_id, public_pem_path, private_pem_path, expire_time, status)values('#{now_time}', '#{now_time}', '#{developerId}', '#{certificate_id}', '#{publicPemPath}', '#{privatePemPath}', '#{eTime}', '1')")
	end
rescue Exception  => e
	puts "Trace message: #{e.errstr}"
else
	puts "Success message: 保存证书成功"
ensure
	# 断开与服务器的连接
	client.close if client
end
