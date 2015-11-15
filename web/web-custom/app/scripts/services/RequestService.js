'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:RequestService
 * @description
 * # RequestService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('RequestService', function($http,$q ) {

    
    /*Atributes*/
    this.serverIp = "http://localhost:9000";

    /*Functions*/

    this.callApi = function(requestData) {
    	var deferred  = $q.defer();
        var request = $http({
            method: requestData.method,
            contentType: 'text/plain',
            url: this.serverIp + requestData.url,
            params: requestData.params
        })
        .success( function(response){
            deferred.resolve(response);     
        })
        
        .error( function(response){
            deferred.reject(response);
        });
        return deferred.promise;
	}


});
