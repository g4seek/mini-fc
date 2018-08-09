# -*- coding: utf-8 -*-
import json
import urllib

import requests


def handler(environ, start_response):
    query_string = environ["QUERY_STRING"]
    params = parse_param(query_string)
    response_body = ""
    if "city" in params:
        try:
            city_name = urllib.unquote(params.get("city"))
            city_code = get_code_by_name(city_name)
            weather_info = get_weather_by_code(city_code)
            response_body = weather_info
        except Exception as e:
            response_body = e.message
    else:
        response_body = "需要输入参数:city"

    status = '200 OK'
    response_headers = [('Content-type', 'text/html; charset=utf-8')]
    start_response(status, response_headers)
    return response_body


def get_code_by_name(city_name):
    city_map = {"北京": "101010100", "上海": "101020100", "天津": "101030100", "重庆": "101040100", "济南": "101120101",
                "石家庄": "101090101", "长春": "101060101", "哈尔滨": "101050101", "沈阳": "101070101", "呼和浩特": "101080101",
                "乌鲁木齐": "101130101", "兰州": "101160101", "银川": "101170101", "太原": "101100101", "西安": "101110101",
                "郑州": "101180101", "合肥": "101220101", "南京": "101190101", "杭州": "101210101", "福州": "101230101",
                "广州": "101280101", "南昌": "101240101", "海口": "101310101", "南宁": "101300101", "贵阳": "101260101",
                "长沙": "101250101", "武汉": "101200101", "成都": "101270101", "昆明": "101290101", "拉萨": "101140101",
                "西宁": "101150101", "台北": "101340101", "香港": "101320101", "澳门": "101330101"}
    if city_name not in city_map:
        raise Exception(
            "当前城市无法查询,城市列表:北京,上海,天津,重庆,济南,石家庄,长春,哈尔滨,沈阳,呼和浩特,乌鲁木齐,兰州,银川,太原,西安,郑州,合肥,南京,杭州,福州,广州,南昌,海口,南宁,贵阳,长沙,武汉,成都,昆明,拉萨,西宁,台北,香港,澳门")
    return city_map.get(city_name)


def get_weather_by_code(city_code):
    url = "http://www.weather.com.cn/data/cityinfo/{0}.html".format(city_code)
    content = requests.get(url).content
    data = json.loads(content)
    if not data["weatherinfo"]:
        raise Exception("无天气信息")
    temp1 = data["weatherinfo"]["temp1"].encode('utf-8')
    temp2 = data["weatherinfo"]["temp2"].encode('utf-8')
    weather = data["weatherinfo"]["weather"].encode('utf-8')
    city = data["weatherinfo"]["city"].encode('utf-8')
    weather_info = "城市:{0},天气:{1},最高气温:{2},最低气温:{3}".format(city, weather, temp2, temp1)
    return weather_info


def parse_param(query_string):
    params = {}
    query_string = query_string + "&"
    param_pairs = query_string.split("&")
    for param_pair in param_pairs:
        if param_pair.find("=") > 0:
            param_name = param_pair.split("=")[0]
            param_value = param_pair.split("=")[1]
            params[param_name] = param_value
    return params
