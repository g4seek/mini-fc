# -*- coding: utf-8 -*-
import json
import requests
import urllib


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
    city_map = {"北京": "2", "上海": "39", "天津": "24", "重庆": "52", "济南": "1399",
                "石家庄": "1742", "长春": "182", "哈尔滨": "250", "沈阳": "94", "呼和浩特": "2036",
                "乌鲁木齐": "2505", "兰州": "2331", "银川": "2299", "太原": "1909", "西安": "2182",
                "郑州": "379", "合肥": "1547", "南京": "1045", "杭州": "1185", "福州": "1654",
                "广州": "886", "南昌": "1286", "海口": "1020", "南宁": "776", "贵阳": "2982",
                "长沙": "650", "武汉": "537", "成都": "2635", "昆明": "2827", "拉萨": "3084",
                "西宁": "2440", "台北": "3179", "香港": "3173", "澳门": "3176"}
    if city_name not in city_map:
        raise Exception(
            "当前城市无法查询,城市列表:北京,上海,天津,重庆,济南,石家庄,长春,哈尔滨,沈阳,呼和浩特,乌鲁木齐,兰州,银川,太原,西安,郑州,合肥,南京,杭州,福州,广州,南昌,海口,南宁,贵阳,长沙,武汉,成都,昆明,拉萨,西宁,台北,香港,澳门")
    return city_map.get(city_name)


def get_weather_by_code(city_code):
    url = 'http://freecityid.market.alicloudapi.com/whapi/json/alicityweather/briefcondition'
    app_code = 'ce5df0b3232c45f6a3c843808e0300a8'
    token = '46e13b7aab9bb77ee3358c3b672a2ae4'

    headers = {'Authorization': 'APPCODE ' + app_code}
    body = {'cityId': city_code, 'token': token}
    content = requests.post(url=url, data=body, headers=headers).content
    data = json.loads(content)
    if not data["data"]:
        raise Exception("无天气信息")
    city = data["data"]["city"]["name"].encode('utf-8')
    condition = data["data"]["condition"]["condition"].encode('utf-8')
    temp = data["data"]["condition"]["temp"]
    windDir = data["data"]["condition"]["windDir"].encode('utf-8')
    windLevel = data["data"]["condition"]["windLevel"]
    weather_info = "城市:{0},天气:{1},温度:{2}℃,风力:{3}{4}级".format(city, condition, temp, windDir, windLevel)
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
