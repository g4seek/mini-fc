# -*- coding: utf-8 -*-
import json


def handler(environ, start_response):
    response_body = "hello, function compute!"
    status = '200 OK'
    response_headers = [('Content-type', 'text/plain')]
    start_response(status, response_headers)
    return [json.dumps(response_body)]
