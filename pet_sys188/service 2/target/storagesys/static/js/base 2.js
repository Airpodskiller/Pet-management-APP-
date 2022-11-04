; (function (window, $) {
    //提交数据返回时的参数
    $.msg = function (data,success,fail) {
        if (data.State == 0) {
            layer.msg(data.Content, { icon: 1 }, function () {
                if (success != undefined && typeof (success) == "function") {
                    success(data);
                }
            });
        } else if (data.State == 1) {
            layer.msg(data.Content, { icon: 1 }, function () {
                if (fail != undefined && typeof (fail) == "function") {
                    fail(data);
                }
            });
        } else if (data.State == 3) {
            layer.msg(data.Content, function () {
                //判断是否有父级节点
                var parent = window;
                while (parent.parent !== undefined) {
                    parent = parent.parent;
                }
                parent.location.href = data.Data;
            });
        } else {
            layer.msg(data.Content);
        }
    };

})(window, jQuery);

