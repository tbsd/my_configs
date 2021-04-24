#!/bin/bash
scrot -z /tmp/lockscreen.png
convert -scale 10% -scale 1000% /tmp/lockscreen.png /tmp/pixelated.png
i3lock -i /tmp/pixelated.png -u
rm /tmp/lockscreen.png /tmp/pixelated.png
