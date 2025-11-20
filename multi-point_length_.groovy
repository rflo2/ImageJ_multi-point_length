#@ImagePlus image

// Rémy Flores-Flores - INSERM - I2MC
// Mesure the cumulative length and range from multipoint annotation on a 3D images
// Cumulative Length : summed distance between each point
// Range : distance between first and last point
// How to use : 	1. draw the point with the multipoint tool to follow a structure in 2D or 3D
// 					2. run the script. It will add the poitns to the ROI manager (for saving), add the mesurement to results table and delete the poitns from the image
// 					3. Draw a new points. Do not select precedent points in the ROI manager as it will continue adding points to these.

import ij.ImagePlus
import ij.IJ
import ij.plugin.frame.RoiManager
import ij.measure.ResultsTable

roi = image.getRoi()
number = roi.nPoints

points = roi.getPolygon()
X = points.xpoints
Y = points.ypoints

Z=[]
for(int i in 0..number-1){
	Z.add(roi.getPointPosition(i))}

rm = RoiManager.getRoiManager()
rm.addRoi(roi)
ind = rm.getCount()
rm.rename(ind-1,ind.toString())
image.deleteRoi()

cal = image.getCalibration()
s_xy = cal.pixelWidth
s_z = cal.pixelDepth

length = 0
for(int i in 0..number-2){
	length = length + Math.sqrt(((X[i+1]-X[i])*s_xy)**2+((Y[i+1]-Y[i])*s_xy)**2+((Z[i+1]-Z[i])*s_z)**2)
}
range = Math.sqrt(((X[-1]-X[0])*s_xy)**2+((Y[-1]-Y[0])*s_xy)**2+((Z[-1]-Z[0])*s_z)**2)

rt = ResultsTable.getResultsTable()
rt.incrementCounter()
rt.addValue("index",ind)
rt.addValue("Number",number)
rt.addValue("Length µm",length)
rt.addValue("range µm",range)
rt.show("results")
