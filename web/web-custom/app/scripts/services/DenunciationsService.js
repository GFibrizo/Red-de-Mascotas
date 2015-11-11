'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:DenunciationsService
 * @description
 * # DenunciationsService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('DenunciationsService', function($http,$q ,RequestService) {

    
    /*Atributes*/
    
    /*Functions*/

    this.getDenunciations = function(filters) {
    	var deferred  = $q.defer();
        $http.get('mocks/denuncias.json').then(
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