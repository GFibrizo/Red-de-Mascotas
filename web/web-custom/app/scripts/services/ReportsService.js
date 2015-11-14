'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:ReportsService
 * @description
 * # ReportsService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('ReportsService', function($http,$q ,RequestService) {

    
    /*Atributes*/
    

    /*Functions*/
    ///reports?fromDate=2015/11/08&toDate=2015/11/15 
    this.getData = function(filters) {
        var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url: "/reports",
            params: filters
        };
        RequestService.callApi(requestData)
        .then(
            function successCallback(response) {
                deferred.resolve(response);
            }, 
            function errorCallback(response) {
                deferred.reject(response);
            }
        );
        return deferred.promise;
    }


    this.getNameByCheck =  function(filterPetName){
        if (filterPetName == "Ambos")
            return;
        else
            return filterPetName;   
    }
 
 
});