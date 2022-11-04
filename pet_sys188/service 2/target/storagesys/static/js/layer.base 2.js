; (function (window, $, layer) {
    window.Icon = {};
    window.Icon.Msg = {
        ok: 1,
        error:2
    };
    if (layer) {
        //修改window的alert方法
        var $alert = window.alert;
        var $confirm = window.confirm;
        window.alert = function () {
            if (arguments.length <= 1) {
                $alert(arguments[0]);
            } else {
                var type = typeof (arguments[1]);
                if (type == "number") {
                    layer.msg(arguments[0], { icon: arguments[1],time:1000 });
                } else if (type == "object") {
                    layer.msg(arguments[0], arguments[1]);
                } else if(type == "function"){
                    layer.msg(arguments[0], { time: 1000 }, arguments[1]);
                }
            }
        };
        window.alertS = function (msg, fn) {
            var type = typeof (fn);
            var time = 1000;
            var callback = function () {

            };
            if (fn) {
                if (type == "function") {
                    callback = fn;
                } else if (type == "number") {
                    time = fn;
                }
            }
            
            layer.msg(msg + "", { icon: 1, time: time }, function () {
                callback();
            });
        };
        window.alertE = function (msg, fn) {
            var type = typeof (fn);
            var time = 1000;
            var callback = function () {

            };
            if (fn) {
                if (type == "function") {
                    callback = fn;
                } else if (type == "number") {
                    time = fn;
                }
            }

            layer.msg(msg + "", { icon: 2, time: time }, function () {
                callback();
            });
        };
        //修改window的confirm方法

        window.Enum = {};
        window.Enum.msg = {};
    }
})(window,jQuery,layer);