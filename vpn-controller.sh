#!/bin/bash

endpoint="http://localhost:9000/vpn"
server_suffix=".nordvpn.com"

function call_api() {
    local method=$1
    local path=$2
    curl --silent -X $method "$endpoint$path" #| python -m json.tool
}

function format_server() {
    local json="$1"
    local country=`echo "$json" | jq -r .serverId.country.code`
    local cc=${country,,}
    local num=`echo "$json" | jq -r .serverId.number`
    local load=`echo "$json" | jq -r .networkLoad`
    local proto=`echo "$json" | jq -r .protocol`

    echo "$cc$num$server_suffix"
    echo "load: $load"
    echo "protocol: $proto"
}

command=$1
shift

case "$command" in

best)
    format_server `call_api GET /country/$1/best`
    ;;

active)
    format_server `call_api GET /active`
    ;;

switch-to-better)
    format_server `call_api PUT /switch-to/better`
    ;;

switch-country)
    format_server `call_api PUT /switch-to/country/$1`
    ;;

*)
    echo RTFS
    ;;

esac