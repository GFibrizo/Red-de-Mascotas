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

    this.getUsers = function(filters) {
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
	}
 
 
});