
function replace_this_shit() {
  for f in *.$1.ovpn; do
    nf="`basename -s $2.ovpn $f`.conf"
    cat $f | sed "s/^auth-user-pass$/auth-user-pass auth.txt/" > $nf
  done
}

replace_this_shit tcp443 443
replace_this_shit udp1194 1194

