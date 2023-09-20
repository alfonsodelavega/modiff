#!/usr/bin/env bash

modiff_jar=$(realpath ../org.eclipse.epsilon.modiff/target/org.eclipse.epsilon.modiff-0.0.1-SNAPSHOT-shaded.jar)

repairshop_metamodel=$(realpath metamodel/repairshop.ecore)

declare -a scenarios=("s00" "s01")

from=$(realpath scenarios/s00/s00-from.model)
to=$(realpath scenarios/s00/s00-to.model)

for s in "${scenarios[@]}"
do
    from=$(realpath scenarios/${s}/${s}-from.model)
    to=$(realpath scenarios/${s}/${s}-to.model)

    # standard diff over xmi
    diff -u $from $to > scenarios/${s}/${s}_xmidiff.diff

    # modiff + munidiff (requires mvn clean verify on org.eclipse.epsilon.modiff)
    java -jar ${modiff_jar} -from=${from} -to=${to} -ecore=${repairshop_metamodel} > scenarios/${s}/${s}_munidiff.diff
done
