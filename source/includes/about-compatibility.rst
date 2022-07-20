We include compatibility tables for each version of the driver to guide
your decisions on what versions you need to ensure your environment
remains fully operational.

We maintain the following tables for each driver:

- :ref:`About MongoDB compatibility <mongodb-compatibility-table-about-{+driver+}>`
- :ref:`About language compatibility <language-compatibility-table-about-{+driver+}>`

Read the sections below for detailed explanations of each table.

.. _mongodb-compatibility-table-about-{+driver+}:

About MongoDB Compatibility
~~~~~~~~~~~~~~~~~~~~~~~~~~~

In the **MongoDB Compatibility** table, the columns are labeled with
versions of MongoDB server and the rows are labeled with major release
versions of the driver.

The check marks (✓) indicate that the driver can access **all the
features** of that specific version of MongoDB server unless those features
have been deprecated or removed.

If you are **upgrading your server version**, your current driver should
continue to function properly, but may not be able to access the features
introduced in the newer server versions. We recommend using the newest
compatible driver when upgrading your server version.

If you are **upgrading your driver version**, you can use the table to
identify which version you need to access all the newest features included
in a specific version of the server. Also note that any of the minor
or patch versions share the same compatibility as the major version
release.

We recommend that you avoid using older versions of the drivers that do not
appear on the chart because they are unsupported.

.. note::

   Although the drivers do not advertise compatibility with the versions of
   the server released after them, they are usually interoperable.

.. _language-compatibility-table-about-{+driver+}:

About Language Compatibility
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In the **Language Compatibility** table, the columns are labeled with
versions of the language (e.g. Node.js, Python, etc.) and the rows are
labeled with major release versions of the driver.

The check marks (✓) indicate that the code in the driver is fully
compatible with the version of the language.
