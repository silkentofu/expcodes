# -*- coding: utf-8 -*-
__author__ = 'EXP (272629724@qq.com)'
__date__ = '2018-03-29 20:44'

import configparser

config = configparser.ConfigParser()
config.read('D:/01_workspace/python/pycharm/conf/url.ini')


# 获取登陆用SIG的URL
SIG_URL = config.get('lander', 'SIG_URL')

# 获取登陆验证码的URL
VCODE_URL = "https://ssl.ptlogin2.qq.com/check"

# 获取登陆验证码图片的URL
VCODE_IMG_URL = "https://ssl.captcha.qq.com/getimage"

# QQ空间登陆URL
XHR_LOGIN_URL = "https://ssl.ptlogin2.qq.com/login"

# QQ空间域名地址(前缀)
QZONE_DOMAIN = "https://user.qzone.qq.com/"

# QQ空间地址
# QZONE_HOMR_URL = %(QZONE_DOMAIN)s%(QQ)s

# 获取相册列表URL
ALBUM_LIST_URL = "https://h5.qzone.qq.com/proxy/domain/photo.qzone.qq.com/fcgi-bin/fcg_list_album_v3"

# 相册地址
# ALBUM_URL = %(QZONE_HOMR_URL)s/photo/%(AID)s

# 获取照片列表URL
PHOTO_LIST_URL = "https://h5.qzone.qq.com/proxy/domain/photo.qzone.qq.com/fcgi-bin/cgi_list_photo"

#获取说说分页内容URL
MOOD_URL = "https://h5.qzone.qq.com/proxy/domain/taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6"

# 说说引用地址
MOOD_REFERER = "https://qzs.qq.com/qzone/app/mood_v6/html/index.html"

#说说域名地址
MOOD_DOMAIN = "http://taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6"
