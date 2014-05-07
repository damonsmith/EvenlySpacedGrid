EvenlySpacedGrid
================

Android implementation of an Evenly Spaced Grid ViewGroup 

Usage
----- 
1. import module, 
2. add it as library project for your app
3. add xmlns:evenlySpacedGrid="http://schemas.android.com/apk/res-auto" to the root view in your layout
3. copy the xml for EvenlySpacedGrid from example.xml into your layout 

How it works
------------
EvenlySpacedGrid assumes all child views are the same size, and have a set width and height.

It spaces out the elements so that they are evenly spaced into rows and columns, fitting as many as it
can into a row before going to the next row.

