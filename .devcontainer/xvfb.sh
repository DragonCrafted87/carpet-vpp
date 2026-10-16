#!/bin/bash
if ! pgrep -f 'Xvfb :99' >/dev/null 2>&1; then
    Xvfb :99 -screen 0 1280x720x24 >/tmp/xvfb.log 2>&1 &
fi
