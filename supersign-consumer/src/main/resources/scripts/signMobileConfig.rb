require "spaceship"
require "mysql2"
require 'pathname'

sourceMobileConfig = ARGV[0].to_s;
targetMobileConfig = ARGV[1].to_s;
mbCrtPath = ARGV[2].to_s;
mbKeyPath = ARGV[3].to_s;
mbPemPath = ARGV[4].to_s;

begin
	system "openssl smime -sign -in  #{sourceMobileConfig} -out #{targetMobileConfig} -signer #{mbCrtPath} -inkey #{mbKeyPath} -certfile #{mbPemPath} -outform der -nodetach"
rescue Exception  => e
	puts "Trace message: #{e.errstr}"
else
	puts signMobileConfig
ensure
	# 断开与服务器的连接
end