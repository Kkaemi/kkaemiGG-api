let view = {

    init : function() {

        const _this = this;

        let oldVal;

        $('#writeComment').on('propertychange change keyup paste input', function() {

            let currentVal = $(this).val();
            if (currentVal === oldVal) { return; }
            oldVal = currentVal;

            if (currentVal.length > 1000) {
                $(this).val(currentVal.substring(0, 1000));
                $(this).css('height', 'auto');
                $(this).height(this.scrollHeight);
                $('#count').html($(this).val().length);
                return;
            }

            $(this).css('height', 'auto');
            $(this).height(this.scrollHeight);
            $('#count').html(currentVal.length);

        });

        $('#commentSubmitButton').on('click', function() {
            _this.submitComment();
        });

        $('#commentRefreshButton').on('click', function() {
            _this.refreshComment();
        });

    },

    submitComment : function() {
        if (!$('#writeComment').val().trimLeft().trimRight()) {
            alert('내용을 입력해 주세요!!!');
            $('#writeComment').focus();
        }
    },

    refreshComment : function() {}

};

view.init();