#!/bin/bash

function replace_this_shit() {
  for f in *.$1.ovpn; do
    nf="`basename -s $2.ovpn $f`.conf"
    cat $f | sed "s/^auth-user-pass$/auth-user-pass auth.txt/" > $nf
  done
}

replace_this_shit tcp443 443
replace_this_shit udp1194 1194

echo "Notes for Arch Linux users (may work for other distros):"
echo "1. Move all files to /etc/openvpn/client/"
echo "2. Create a file /etc/openvpn/client/auth.txt with your
echo "   username and password in separate lines"
