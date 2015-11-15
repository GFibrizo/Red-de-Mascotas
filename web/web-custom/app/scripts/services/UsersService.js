'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:UsersService
 * @description
 * # UsersService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('UsersService', function($http,$q ,RequestService) {

    
    /*Atributes*/
    
    /*Functions*/

/*    this.getUsers = function(filters) {
        var deferred  = $q.defer();
        $http.get('mocks/users.json').then(
            function (response) {               
                
                deferred.resolve(response);
            },
            function (response) {               
                deferred.reject(response);
            }
        );
        return deferred.promise;
    }*/
    this.getUsers = function(filters) {
    	var deferred  = $q.defer();
        var requestData =  {
            method: "GET",
            url: "/users/denunciations"
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
 
    this.blockUser = function(user){
     var deferred  = $q.defer();
        console.log(user.id);
        var requestData =  {
            method: "PUT",
            url: "/user/" + user.id + "/block"
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

    this.unBlockUser = function(user){
        var deferred  = $q.defer();
        var requestData =  {
            method: "PUT",
            url: "/user/" + user.id + "/unblock"
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
 
});
