window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

window.ajax = function (data, hidden) {
    $.ajax({
        type: "POST",
        dataType: "json",
        data: data,
        success: hidden
    });

}