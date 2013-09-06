out = file("classifiers.xml", "w")

out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
out.write("<classifiers>\n")


cnt = 0

for c in xrange(1, 100, 5):
    for m in xrange(2, 20, 1):
        out.write("\t<classifier name=\"J48\">\n")
        out.write("\t\t<parameter>\n")
        out.write("\t\t\t<class>\n")
        out.write("\t\t\t\tweka.classifiers.trees.J48\n")
        out.write("\t\t\t</class>\n")
        out.write("\t\t\t<options>\n")
        out.write("\t\t\t\t-C " + str(c/100.0) + " -M " + str(m) + "\n")
        out.write("\t\t\t</options>\n")
        out.write("\t\t</parameter>\n")
        out.write("\t</classifier>\n")
        cnt += 1
out.write("</classifiers>\n")
out.close()
print cnt
