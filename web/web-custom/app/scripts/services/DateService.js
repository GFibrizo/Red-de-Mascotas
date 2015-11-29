'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.sercice:DateService
 * @description
 * # DateService
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .service('DateService', function() {

    
    /*Atributes*/
    
    /*Functions*/


    this.toServerFormat = function(date) {
        var day = date.getDate();
        var month = date.getMonth() + 1;
    	var year = date.getFullYear();

        // return year + "/" + month + "/" + day; 
        return day + "/" + month + "/" + year; 
	}
 
 
});