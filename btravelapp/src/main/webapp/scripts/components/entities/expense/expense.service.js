'use strict';

angular.module('btravelappApp')
    .factory('Expense', function ($resource, DateUtils) {
        return $resource('api/expenses/:id', {}, {
            //'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
angular.module('btravelappApp')
	.factory('ExpenseBtr', function($http){
		return {
			getAllExpensesByBtrId : function(id){
	    	    return $http.get('api/expensesbtr/'+id).then(function(response) {
	    	        return response.data;
	    	    });
	    	 }
		}
	});