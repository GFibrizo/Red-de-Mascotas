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

    this.getData = function(filters) {
        var deferred  = $q.defer();
        $http.get('mocks/reporteAltas.json').then(
            function (response) {               
                
                deferred.resolve(response);
            },
            function (response) {               
                deferred.reject(response);
            }
        );
        return deferred.promise;
    }

    this.getAverageFromAdoption = function(filters) {
        var deferred  = $q.defer();
        $http.get('mocks/reporteTiempoAdopcion.json').then(
            function (response) {               
                
                deferred.resolve(response);
            },
            function (response) {               
                deferred.reject(response);
            }
        );
        return deferred.promise;
    }

    this.getAverageFromFounded = function(filters) {
    	var deferred  = $q.defer();
        $http.get('mocks/reporteTiempoHallazgo.json').then(
            function (response) {               
                
                deferred.resolve(response);
            },
            function (response) {               
                deferred.reject(response);
            }
        );
        return deferred.promise;
	}

    this.getNameByCheck =  function(object){
            if (object.both)
                    return "both";
            if (object.dogs)
                    return "dogs";
            if (object.cats)
                    return "cats";
    }
 
 
});