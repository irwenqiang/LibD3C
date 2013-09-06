out = file("classifiers_ann.xml", "w")

out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
out.write("<classifiers>\n")


cnt = 0

for H in {'a', 'i', 'o', 't'}:
    for L in xrange(1,10):
        for M in xrange(1, 10):
            out.write("\t<classifier name=\"ANN\">\n")
            out.write("\t\t<parameter>\n")
            out.write("\t\t\t<class>\n")
            out.write("\t\t\t\tweka.classifiers.functions.MultilayerPerceptron\n")
            out.write("\t\t\t</class>\n")
            out.write("\t\t\t<options>\n")
            out.write("\t\t\t\t-L " + str(L/10.0) + " - M " + str(M/10.0) + " -H " + H + "\n")
            out.write("\t\t\t</options>\n")
            out.write("\t\t</parameter>\n")
            out.write("\t</classifier>\n")
            cnt += 1
            
out.write("</classifiers>\n")
out.close()
print cnt
