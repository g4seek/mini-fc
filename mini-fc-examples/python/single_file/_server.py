# coding=utf-8

from wsgiref.simple_server import make_server

from weather import handler

httpd = make_server('', 8000, handler)
print "Serving HTTP on port 8000..."
httpd.serve_forever()
