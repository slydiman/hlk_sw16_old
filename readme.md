This is a fork of [hlk_sw16](https://github.com/home-assistant/core/tree/dev/homeassistant/components/hlk_sw16) and [protocol](https://github.com/jameshilliard/hlk-sw16), created by me to support the early version of [Hi-Link](http://www.hlktech.net/) **[HLK-SW16](http://www.hlktech.net/product_detail.php?ProId=48)** without RTC clock in **Home Assistant**.
The relay control protocol is little bit different.
The core component documentation is [here](https://www.home-assistant.io/integrations/hlk_sw16/).

![](hlk_sw16_old.jpg)

Thanks to @jameshilliard for the initial code.

## Install

Download [Source code (hlk_sw16_old-0.0.1.zip)](https://github.com/slydiman/hlk_sw16_old/releases/latest), extract and copy the entire folder `hlk_sw16_old-0.0.1` folder to `custom_components` in your `config` folder.

## Bonus

[hlk_sw16_cmd.java](hlk_sw16_cmd.java) is a simple command line tool to demostrate the protocol.
