#!/bin/bash
git pull
pip2 install -r requirements.txt
ps aux | grep 'ops_api' | awk '{print $2}' | xargs kill -9
nohup python ops_api.py &