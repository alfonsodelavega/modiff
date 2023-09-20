#!/usr/bin/env bash

modiff_jar=$(realpath ../org.eclipse.epsilon.modiff/target/org.eclipse.epsilon.modiff-0.0.1-SNAPSHOT-shaded.jar)

repairshop_metamodel=$(realpath metamodel/repairshop.ecore)

from=$(realpath scenarios/s00/s00-from.model)
to=$(realpath scenarios/s00/s00-to.model)

java -jar ${modiff_jar} -from=${from} -to=${to} -ecore=${repairshop_metamodel}
