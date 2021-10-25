function bs_input_file() {
    $(".input-file").before(
        function() {
            if (!$(this).prev().hasClass('input-ghost')) {
                var element = $(".input-ghost");
                element.change(function(){
                    element.next(element).find('input').val((element.val()).split('\\').pop());
                });
                $(this).find("button.btn-default").click(function(){
                    element.click();
                });
                $(this).find("button.btn-reset").click(function(){
                    element.val(null);
                    $(this).parents(".input-file").find('input').val('');
                });
                $(this).find('input').css("cursor","pointer");
                $(this).find('input').mousedown(function() {
                    $(this).parents('.input-file').prev().click();
                    return false;
                });
                return element;
            }
        }
    );
}
function delFile(fileName, delUrl) {
    $.confirm({
        title: 'Delete?',
        content: fileName,
        type: 'red',
        typeAnimated: true,
        buttons: {
            tryAgain: {
                text: 'delete',
                btnClass: 'btn-red',
                action: function(){
                    window.location.href = delUrl
                }
            },
            close: function () {
            }
        }
    });
}
$(function() {
    if (window.history && window.history.pushState) {
        $(window).on('popstate', function () {
            window.history.pushState('forward', null, '#');
            window.history.forward(1);
            message = null;
        });
    }
    window.history.pushState('forward', null, '#');
    window.history.forward(1);

    if (message != undefined && message != null && message !== ' ') {
        if (message === 'success') {
            Qmsg.success(message);
        } else {
            Qmsg.error(message);
        }
        message = null;
    }
    bs_input_file();
});