'use strict';

angular.module('btravelappApp')
	.controller('BtrApproveController', function($scope, $uibModalInstance,$http, entity, Btr, manager) {

		$scope.user = entity;
        $scope.btr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
        var onSaveSuccess = function (result) {
            $scope.$emit('btravelappApp:btrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };
        
        /*var result = function(id){
			return $http.get('api/btrs/btr/approve/'+id).then(function(response){
				return response.data;
			})
		};*/
        
       /* var isManager = function(result){
        	result = $scope.user.idManager;
       }*/
        
        $scope.confirmApprove = function (id) {
        	$scope.isSaving = true;	
        	console.log($scope.btr.id); 	// returneaza id-ul btr-ului
        	console.log($scope.btr.user.idManager); // manager
        	console.log($scope.btr.assigned_to.idManager); // manager2
        	//if($scope.btr.user.idManager==null){
        	
        	if( $scope.btr.assigned_to.idManager != null )
	        	{
        			
        		// TREBUIE SA VAD CUM MODIFIC ASSIGNED_TO DIN MANAGER IN MANAGER2 
        			//(adik din manager in manager2 <<manager-ul lui manager>>)
        		
        	
        	/*
        		var managermic = function(id){
        			return $http.get('api/users/user/'+id).then(function(response){
        				console.log(response.data);
        				return response.data;})
        		}
        		console.log(response.data);
        		*/
        		/* o alta incercare	
        		var managermare = function(){			
        			return $http.get('/users/'+$scope.btr.assigned_to.idManager).then(function(response){
        				debugger;
        				console.log(idManager);
        				$scope.btr.assigned_to=response.data;
        				console.log(response.data); // tot obiectul manager2
        				return response.data;
        			})
        		}
        			*/
        		/* asta sa o mut in exterior ( acum incerc in btrapprove.service.js )
        		$http.get('api/users/'+$scope.btr.assigned_to.idManager).then(function(response){
        				debugger;
        				$scope.btr.assigned_to=response.data;
        				console.log(response.data); // tot obiectul manager2
        				return response.data;
        			});
        			*/
        	

        		 
	        		$scope.btr.assigned_from = $scope.btr.manager;
	        		
	        		debugger;
	        		$scope.btr.assigned_to = manager;

	        		console.log($scope); 
	        		console.log($scope.btr.assigned_to); 
	        		debugger;
	        		
	        		
	        		/* dintr-o bucata
	        		var idManager = $scope.btr.assigned_to.idManager;
	        		$scope.btr.assigned_to = function(idManager){			
	        			return $http.get('api/users/'+idManager).then(function(response){
	        				console.log(response.data); // tot obiectul manager2
	        				return response.data;
	        			})
	        		};
	        		*/
	        	}    
        	else
        		{
        			$scope.btr.status="Issuing ticket";//get the object instead of login
        			$scope.btr.assigned_from = $scope.btr.manager;
        			$scope.btr.assigned_to = $scope.btr.supplier;  
        			
        		}       			
        	
        	Btr.update($scope.btr, onSaveSuccess, onSaveError);
        	
        	
        	console.log($scope.btr.assigned_from); // manager
        	console.log($scope.btr.assigned_to); 
        	
        
        	
        	//Btr.update($scope.btr, onSaveSuccess, onSaveError);
        	//}
        	//else
        	//	{
        		//console.log($scope.btr.assigned_to.login);
        		
        		
        		//console.log($scope.btr.manager); // supplier
        		//console.log($scope.btr.supplier); //manager
        		//console.log($scope.btr.user); //user
        		
        		
        		//Not ok yet
        		//Aici ar trebuie sa pun manager in loc de supplier la assigned_from
        		//$scope.btr.assigned_from.login = $scope.btr.assigned_to.login; 
        		
        		//Aici ar trebuie sa pun manager2 in loc de manager la assigned_to
        		//$scope.btr.assigned_to.login = $scope.btr.assigned_to.idManager; 
        		
        		
        		
        		
        		//}     	
        };
        
    });
