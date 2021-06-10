You can pass a verbosity level to the ``explain()`` method. The
verbosity level specifies the level of detail for the explanation. 

The following table shows the verbosity levels for the
explain operation and their intended use cases:

.. list-table::
   :header-rows: 1
   :stub-columns: 1
   :widths: 40 60

   * - Verbosity Level
     - Use Case

   * - ALL_PLANS_EXECUTIONS
     - You want to know which plan MongoDB will choose to run your query.

   * - EXECUTION_STATS
     - You want to know if your query is performing well.

   * - QUERY_PLANNER
     - You have a problem with your query and you want as much information
       as possible to diagnose the issue.
     