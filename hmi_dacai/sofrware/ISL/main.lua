--下面列出了常用的回调函数
--更多功能请阅读<<物联型LUA脚本API.pdf>>

--初始化函数
--function on_init()
--end

--定时回调函数，系统每隔1秒钟自动调用。
--function on_systick()
--end

--用户通过触摸修改控件后，执行此回调函数。
--点击按钮控件，修改文本控件、修改滑动条都会触发此事件。
--function on_control_notify(screen,control,value)
--end

--当画面切换时，执行此回调函数，screen为目标画面。
--function on_screen_change(screen)
--end