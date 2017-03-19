$(document).ready(function () {
//    $('.toggle').on('click', function () {
//        $('.container').stop().addClass('active');
//    });
//
//    $('.close').on('click', function () {
//        $('.container').stop().removeClass('active');
//    });
//    
//    
//    
//   
    //Input error begin
    var inputerror = function (t) {
        $(t).addClass('inputerror', 1000);
        setTimeout(function () {
            $(t).removeClass('inputerror');
        }, 1500);
    }

//Input error end
//Phone validator begin
    var phoneValidator = function (phone) {
        var comphn = /^-?\d*\.?\d*$/;
        return comphn.test(phone);
    }
//Phone validator end
    $('#bornDate').datepicker({language: "az"});
    $('#enter').click(function () {
        var id = $('#editPlaceLabel').text().split("#")[1].trim(0);
        var firstName = $('#firstName').val();
        var lastName = $('#lastName').val();
        var fatherName = $('#fatherName').val();
        var phoneNumber = $('#phoneNumber').val();
        var bornDate = $('#bornDate').val();
        var enter = true;
        if (firstName == '') {
            inputerror($('#firstName'));
            enter = false;
        }
        if (lastName == '') {
            inputerror($('#lastName'));
            enter = false;
        }
        if (fatherName == '') {
            inputerror($('#fatherName'));
            enter = false;
        }
        if (bornDate == '') {
            inputerror($('#bornDate'));
            enter = false;
        }
        if (phoneNumber=='' || !phoneValidator(phoneNumber) || phoneNumber.length<10) {
            inputerror($('#phoneNumber'));
            enter = false;
        }
        if( phoneNumber.length < 10 && phoneNumber!='' && phoneValidator(phoneNumber))
        { alert("Telefon nömrənin rəqəmlərinin sayı 10dan kiçik olmamalıdır!!!"); }

        if (enter == true) {
            $.post("addField", {
                id: id,
                firstName: firstName,
                lastName: lastName,
                fatherName: fatherName,
                phoneNumber: phoneNumber,
                bornDate: bornDate
            }).always(function () {
                getFields();
            });
        }
    });
    $('#new').click(function () {
        $('#editPlaceLabel').html('#Yeni');
    });

    function listItemOperations() {
        $('.control[name="edit"]').click(function () {
            var td = $(this).parent().parent().find('td');
            var id = td.eq(0).attr('data');
            var firstName = td.eq(1).html();
            var lastName = td.eq(2).html();
            var fatherName = td.eq(3).html();
            var phoneNumber = td.eq(4).html();
            var bornDate = td.eq(5).html();
            $('#editPlaceLabel').html('#' + id);
            $('#firstName').val(firstName);
            $('#lastName').val(lastName);
            $('#fatherName').val(fatherName);
            $('#phoneNumber').val(phoneNumber);
            $('#bornDate').val(bornDate);
        });

        $('.control[name="delete"]').click(function () {
            var td = $(this).parent().parent().find('td');
            var id = td.eq(0).html();
            var c = confirm("Silməyə əminisiniz?");
            if (c == true) {
                $.post("deleteField", {
                    id: id
                }).always(function () {
                    getFields();
                });
            }
        });


    }
    $("#searchingBox").keyup(function () {
        var search = $(this).val();
        getFields(search);
    });

    getFields();
    function getFields(search) {
        search = (search == undefined) ? '' : search;
        $('.modal-footer button[data-dismiss="modal"]').trigger('click');
        $('.card input').val('');
        $.post("showFields", {
            search: search
        }).done(function (data) {

            $('#fieldlist').html(data);
            listItemOperations();
        });
    }

});