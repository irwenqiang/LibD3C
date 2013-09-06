out = file("classifiers_svm.xml", "w")

out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
out.write("<classifiers>\n")

cnt = 0

for K in {2, 3}:
    for C in {0.000001, 0.00001, 0.0001, 0.001, 0.01, 1.0, 10.0, 100.0, 1000.0}:
        for G in (0.00000001,0.0000001, 0.000001, 0.00001, 0.0001, 0.001, 0.01, 1.0, 10.0):
            out.write("\t<classifier name=\"SVM\">\n")
            out.write("\t\t<parameter>\n")
            out.write("\t\t\t<class>\n")
            out.write("\t\t\t\tweka.classifiers.functions.LibSVM\n")
            out.write("\t\t\t</class>\n")
            out.write("\t\t\t<options>\n")
            out.write("\t\t\t\t-K " + str(K) + " -C " + str(C) + " -G " + str(G) + "\n")
            out.write("\t\t\t</options>\n")
            out.write("\t\t</parameter>\n")
            out.write("\t</classifier>\n")
            cnt += 1
            
out.write("</classifiers>\n")
out.close()
print cnt
