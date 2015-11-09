'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('ReportsCtrl', function($scope,$position,$state) {
      console.log("ReportsCtrl")
      
       $scope.bar = {
          labels: ['2006', '2007', '2008', '2009', '2010', '2011', '2012'],
          series: ['Series A', 'Series B'],

          data: [
             [65, 59, 80, 81, 56, 55, 40],
             [28, 48, 40, 19, 86, 27, 90]
          ]
            
        };

   
  	
  });
