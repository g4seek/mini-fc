# -*- coding: utf-8 -*-


def handler(environ, start_response):
    query_string = environ["QUERY_STRING"]
    params = parse_param(query_string)
    response_body = ""
    if params.has_key("a") and params.has_key("b"):
        a = params["a"]
        b = params["b"]
        try:
            c = float(a) + float(b)
            response_body = str(a) + "+" + str(b) + "=" + str(c)
        except Exception:
            response_body = "a和b都需要为数值"
    else:
        response_body = "需要输入参数a和b"
    status = '200 OK'
    response_headers = [('Content-type', 'text/html; charset=utf-8')]
    start_response(status, response_headers)
    return response_body


def parse_param(query_string):
    params = {}
    query_string = query_string + "&"
    param_pairs = query_string.split("&")
    for param_pair in param_pairs:
        if param_pair:
            param_name = param_pair.split("=")[0]
            param_value = param_pair.split("=")[1]
            params[param_name] = param_value
    return params
