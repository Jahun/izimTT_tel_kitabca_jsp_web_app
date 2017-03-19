<%-- 
    Document   : editPlace
    Created on : 07.Mar.2016, 22:06:10
    Author     : Ceyhun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--EditPlace Modal begin-->
<div class="modal fade" id="editPlace" tabindex="-1" role="dialog" aria-labelledby="editPlaceLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="editPlaceLabel">#1</h4>
            </div>
            <div class="modal-body">
                <div class="container">
                    <div class="card"></div>
                <div class="card">
                    <div class="input-container">
                        <input type="text" id="firstName" required="required"/>
                        <label for="firstName">Ad</label>
                        <div class="bar"></div>
                    </div>
                    <div class="input-container">
                        <input type="text" id="lastName" required="required"/>
                        <label for="lastName">Soyad</label>
                        <div class="bar"></div>
                    </div>
                    <div class="input-container">
                        <input type="text" id="fatherName" required="required"/>
                        <label for="fatherName">Ata Adı</label>
                        <div class="bar"></div>
                    </div>
                    
                    <div class="input-container">
                        <input type="text" id="phoneNumber" required="required"/>
                        <label for="phoneNumber">Telefon Nömrəsi</label>
                        <div class="bar"></div>
                    </div>
                    <div class="input-container">
                        <input type="text" id="bornDate" required="required"/>
                        <label for="bornDate">Doğum Tarixi</label>
                        <div class="bar"></div>
                    </div>
                 
                    </div>
                    </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Bağla</button>
                <button type="button" class="btn btn-primary" id="enter">Daxil et</button>
            </div>
        </div>
    </div>
</div>
<!--EditPlace Modal end-->
