<%-- 
    Document   : list
    Created on : 07.Mar.2016, 21:17:57
    Author     : Ceyhun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../includes/editPlace.jsp" %>
<button class="btn btn-primary" id="new" data-toggle="modal" data-target="#editPlace">Yeni</button>
<form id="exit" method="get"><button type="submit" class="btn btn-warning"  name="exit" value="true">Çıxış</button></form>
<input type="text" class="form-control" placeHolder="Axtar..." id="searchingBox">
<div class="table-responsive">          
    <table class="table table-striped">
        <thead>
            <tr>
                <th>#</th>
                <th>Ad</th>
                <th>Soyad</th>
                <th>Ata Adı</th>
                <th>Telefon Nömrəsi</th>
                <th>Doğum Tarixi</th>
                <th>Redaktə et</th>
                <th>Sil</th>
            </tr>
        </thead>
        <tbody id="fieldlist">
          
        </tbody>
    </table>
</div>

