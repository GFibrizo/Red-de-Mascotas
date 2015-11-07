'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('sbAdminApp')
	.directive('headerNotification',function(LoginService){

		var _controller = function (){
        	var vm = this;
	        vm.logout = function(){            
	            LoginService.logout();
	        }
     
   		}

		return {
	        templateUrl:'scripts/directives/header/header-notification/header-notification.html',
	        restrict: 'E',
	        replace: true,
	        controller: _controller,
	        controllerAs: 'vm',
	        bindToController: true,
    	}
	});


