#!/usr/bin/env bash

# This i3status wrapper allows to add custom information in any position of the statusline
# It was developed for i3bar (JSON format)

# The idea is to define "holder" modules in i3status config and then replace them

# In order to make this example work you need to add
# order += "tztime holder__hey_man"
# and 
# tztime holder__hey_man {
#        format = "holder__hey_man"
# }
# in i3staus config 

# Don't forget that i3status config should contain:
# general {
#   output_format = i3bar
# }
#
# and i3 config should contain:
# bar {
#   status_command exec /path/to/this/script.sh
# }

# Make sure jq is installed
# That's it

# You can easily add multiple custom modules using additional "holders"

function update_holder {

  local instance="$1"
  local replacement="$2"
  echo "$json_array" | jq --argjson arg_j "$replacement" "(.[] | (select(.instance==\"$instance\"))) |= \$arg_j" 
}

function remove_holder {

  local instance="$1"
  echo "$json_array" | jq "del(.[] | (select(.instance==\"$instance\")))"
}

function layout {
  local data="$(xkb-switch -p)"
  local json="{ \"full_text\": \"$data\", \"color\": \"#FFFFFF\"}"
  json_array=$(update_holder holder__layout "$json")
}

function hey_man {
  local frames_total=4
  local frame_data=""
  if [ $frame_num == 0 ] ; then
    frame_data=" ⣀⡾⡛⣿"
  elif [ $frame_num == 1 ] ; then
    frame_data=" ⡠⡛⢷⣾"
  elif [ $frame_num == 2 ] ; then
    frame_data="⠱⣦⣤⣴⣾"
  elif [ $frame_num == 3 ] ; then
    frame_data="⠂⣀⣴⡾⢿"
  fi
  local json="{ \"full_text\": \"$frame_data\", \"color\": \"#FFFFFF\"}"
  json_array=$(update_holder holder__hey_man "$json")
  frame_num=$(((frame_num+1)%frames_total))
}

frame_num=0
i3status | (read line; echo "$line"; read line ; echo "$line" ; read line ; echo "$line" ; while true
do
  read line
  json_array="$(echo $line | sed -e 's/^,//')"
  hey_man
  layout
  echo ",$json_array" 
done)
