# IOS自动化超级签名

IOS自动化重签名系统，每个用户的获取成本至少在7元。

### 系统模块

|  项目   | 名称  |
|  ----  | ---- |
| supersign-admin-server  | 重签名后台管理系统 |
| supersign-common  | 工具类 |
| supersign-interface  | 数据库DAO等 |
| supersign-consumer  | 实际IPA重签名服务 |
| supersign-server  | 重签名前端业务代码 |

### 软件安装

1.安装必要的工具包
```
yum install -y unzip zip git gcc gcc-c++ pcre openssl zlib pcre-devel openssl-devel python-setuptools libffi-devel python-devel.x86_64 openssl-devel ruby ruby-devel expect-devel mariadb-devel mysql-devel

# 升级ruby
yum install centos-release-scl-rh
yum install rh-ruby24* -y


echo "scl enable rh-ruby24 bash" >> /etc/profile
source /etc/profile
```

2.更新gem
```
#查看gem源
gem sources

删除源
gem sources --remove https://rubygems.org/
gem sources -a https://gems.ruby-china.com
```

3.安装fastlane,spaceship
```
gem update --system
gem install fastlane
gem install spaceship

gem install -n /usr/local/bin pry

gem uninstall mysql2
gem install mysql2 --platform=ruby

#启动spaceship(后台运行)
#fastlane spaceship
```

4.更新pip源
```
在user home目录下创建（阿里云服务器不需要）
mkdir .pip
cd .pip

vi pip.conf

#输入一下内容：

[global]
 
index-url = https://pypi.tuna.tsinghua.edu.cn/simple
 
trusted-host = pypi.tuna.tsinghua.edu.cn

#保存即可
```

5.使用fastlane
```
# 初始化目录
fastlane init	(可能会卡5分钟左右，等一下)

出现这些信息就成功了
[11:29:35]:   Learn more about how to automatically generate localized App Store screenshots:
[11:29:35]:             https://docs.fastlane.tools/getting-started/ios/screenshots/
[11:29:35]: ‍✈️  Learn more about distribution to beta testing services:
[11:29:35]:             https://docs.fastlane.tools/getting-started/ios/beta-deployment/
[11:29:35]:   Learn more about how to automate the App Store release process:
[11:29:35]:             https://docs.fastlane.tools/getting-started/ios/appstore-deployment/
[11:29:35]: ‍⚕️  Learn more about how to setup code signing with fastlane
[11:29:35]:             https://docs.fastlane.tools/codesigning/getting-started/
[11:29:35]: 
[11:29:35]: To try your new fastlane setup, just enter and run
[11:29:35]: $ fastlane custom_lane

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
fastlane操作改变后我们再了解一下有哪些配置文件：

Appfile: 存储有关开发者账号相关信息
Fastfile: 核心文件，主要用于 命令行调用和处理具体的流程，lane相对于一个方法或者函数
Deliverfile: deliver工具的配置文件
metadata: 元数据文件夹
Matchfile: Match操作对应的配置文件
screenshots: 截图文件夹
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

# 登录到appstore
fastlane spaceauth -u 3df6vPO9P@gmail.com
让输入6位验证码的时候，请输入  sms
选择 1
再输入新的验证码即可，不然可能登录不成功

登录成功后会让设置环境变量，直接拷贝Example:下面的内容直接在控制台执行即可。
```

6.安装isign
```
pip install --upgrade pip
pip install pymysql

pip install cryptography==2.2.2
pip install pyOpenSSL==18.0.0

git clone https://github.com/apperian/isign.git
cd isign
./INSTALL.sh
```

7.执行脚本上传udid并且下载mobileprovision文件
```
#脚本
ruby addUdid.rb 注册udid
ruby saveCret.rb 拆解线上的p12文件
```

8.执行重签名
```
isign -c /root/applesign/6TQ92MAF2N/client_key.pem -k /root/applesign/6TQ92MAF2N/private_key.pem -p sign.duidui.mobileprovision -o duijiaoyouapp_sign.ipa duijiaoyouapp.ipa
```
