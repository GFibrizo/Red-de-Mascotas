'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:RequestService
 * @description
 * # RequestService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('RequestService', function($http ) {


    this.callApi = function(requestData) {
    	
        var request = $http({
            method: requestData.method,
            contentType: 'application/json',
            url: requestData.url,
            params: requestData.params
        });
        return request;
	}
 
 
});