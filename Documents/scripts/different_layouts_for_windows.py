#!/usr/bin/python3
# Most of this code borrowed from reddit user Deedone

import i3ipc
from subprocess import call
import subprocess
from ctypes import *

windows = {}
default = "us"


def get_win_id(i3):
    focused = i3.get_tree().find_focused()
    ws_name = "%s-%s-%s"%(focused.workspace().num,focused.window_class,focused.id)
    return ws_name


def get_cur():

    curlay = subprocess.Popen("xkb-switch",stdout=subprocess.PIPE).stdout.read()[:-1]
    return curlay.decode("utf-8")

def on_focus(i3,e):
    name = get_win_id(i3)
    lay = windows.get(name,default)
    curlay = get_cur()
    print("cur=",curlay,"desired=",lay)
    if curlay != lay:
        print("switching")
        call(["xkb-switch","-n"])
    print(windows)
    
def fix_caps():
        X11 = cdll.LoadLibrary("libX11.so.6")
        display = X11.XOpenDisplay(None)
        X11.XkbLockModifiers(display, c_uint(0x0100), c_uint(2), c_uint(0))
        X11.XCloseDisplay(display)

def on_binding(i3,e):
    print(e.binding.symbol)
    if e.binding.symbol == "Super_L" and e.binding.command == "nop":
        call(["xkb-switch","-n"])
        curlay = get_cur()
        windows[get_win_id(i3)] = curlay
        print("switch detected")
        print(windows)


i3 = i3ipc.Connection()

i3.on("window::focus",on_focus)
i3.on("binding::run",on_binding)

i3.main()

