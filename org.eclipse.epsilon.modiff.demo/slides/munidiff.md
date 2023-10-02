---
marp: true
theme: default
paginate: true
style: |
    .language-diff {
        font-size: 30pt;
        font-family: 'hack', monospace;
    }
    img {
        display:block;
        margin:auto;
        width: 100%;
    }
    .good {
        color: green;
        font-weight: bold;
    }
    .alert {
        color: chocolate;
        font-weight: bold;
    }


# header: test
# footer: test
# size: 4:3

---
<!-- _paginate: false -->

<style scoped>
p {
    font-size: 28pt;
}
img[alt~="bottom-right"] {
  position: absolute;
  bottom: 40px;
  right: 40px;
}
</style>

# Towards an Interoperable and Customisable Textual Format for Model Differences Reporting

<br>

Alfonso de la Vega (alfonso.delavega@unican.es)
Software Engineering and Real-Time Group
Universidad de Cantabria

<!-- ![bottom-right w:200px](fig/qr.svg) -->

---

# Two-Way Model Comparison

<br>

![](fig/twoway.drawio.svg)

---

# Two-Way Model Comparison: Stages [1]

1. Comparison
2. Differences Representation
3. <span class="good">Differences Reporting</span>

<!-- _footer: "[1] Kolovos et al. Different models for model matching: An analysis of approaches to support model differencing" -->

---

# Example: Repair Shop (metamodel)

![w:800px](fig/repairShop-metamodel-lite.svg)

---

# from.model

![](fig/repairShop-from.svg)

---

# to.model (with changes)

![](fig/repairshop-to.svg)

---

# Graphical diff

The *job1* job has moved from Alice's queue to Bob's

![](fig/repairshop-diff.svg)

---

# Tree-based graphical diff: EMF Compare

![w:1000px](fig/emfcompare.png)

---

# What about plain textual diff?

If there is a custom concrete textual syntax (e.g. Xtext-based) -> <span class="good">Probably great!</span>

<br>

**If no** concrete textual syntax -> <span class="alert">diff the persistence format?</span>

---

# What about plain textual diff? XMI example

Remember: the only change is *job1* moving from Alice's to Bob's queue

```diff
$ diff -u from.model to.model
--- from.model
+++ to.model
@@ -6,6 +6,11 @@
   <workers name="Alice"
       mainSkill="Disassembly"
       secondarySkills="Microsoldering DataRecovery">
+    <queue jobId="job2"
+        description="broken phone screen"/>
+  </workers>
+  <workers name="Bob"
+      mainSkill="Microsoldering">
     <queue jobId="job1"
         description="laptop does not charge">
       <tags>battery</tags>
@@ -14,11 +19,7 @@
           statusId="st1"
           status="pending"/>
     </queue>
-    <queue jobId="job2"
-        description="broken phone screen"/>
   </workers>
-  <workers name="Bob"
-      mainSkill="Microsoldering"/>
   <skills name="Microsoldering"/>
   <skills name="Disassembly"/>
   <skills name="DataRecovery"/>
```

---

# Can we do better than XMI diffs? Munidiff format

```diff
--- from.model
+++ to.model
@@ @@
 Worker "Bob" {
     queue *-->  [
+        Job "job1"
     ]
 }
@@ @@
 Worker "Alice" {
     queue *-->  [
-        Job "job1"
         Job "job2"
     ]
 }
```

---


# <!-- fit --> Why bother? Existing support for the Unified Format

- Markdown code blocks (these slides)
- Editors (e.g. Eclipse, Jetbrains, Visual Studio Code)
- Version Control Systems: GitHub, GitLab, Bitbucket
    - Commits, pull requests, issues, user comments

---

# Support Example: GitHub

![](fig/github.png)

---

# Munidiff: Textual Reporting of Model Differences

- Follows the Unified Format [1]
- Can be integrated with any two-way EMF comparison tool
- Currently:
    - EMF Compare (used for the article)
    - *Modiff* (used in this demo, work in progress)

<!-- _footer: https://www.gnu.org/software/diffutils/manual/html_node/Detailed-Unified.html -->

---

# Munidiff Syntax

<style scoped>
div.twocols {
  margin-top: -10px;
  column-count: 2;
}
div.twocols p:first-child,
div.twocols h1:first-child,
div.twocols h2:first-child,
div.twocols ul:first-child,
div.twocols ul li:first-child,
div.twocols ul li p:first-child {
  margin-top: 0 !important;
}
div.twocols p.break {
  break-before: column;
  margin-top: 0;
}
</style>

<div class="twocols">

```diff
--- from.model
+++ to.model
@@ @@
 Worker "Bob" {
     queue *-->  [
+        Job "job1"
     ]
 }
@@ @@
 Worker "Alice" {
     queue *-->  [
-        Job "job1"
         Job "job2"
     ]
 }
```

<p class="break"></p>

- Element-level chunk granularity
- Elements identified by their type and a custom label
- Features occupy their own lines
    - Facilitates/Respects diff format
</div>

---

# <!-- fit --> How to Use Munidiff: From Model Differences to Text Report

For EMF Compare:
1. Perform the two-way comparison
2. Translate the differences from EMF Compare representation to Munidiff's:
![width:800px](fig/munidiffClasses.svg)

3. Generate a text report from the Munidiff representation

---

# Demo: Find the Differences!

---

# Future Work

- Test different Munidiff flavours
- Exploit more parts of the Unified Format (hunk headers? ``@@  @@``)
    - Summary of the changes in natural language?
- Automatic generation of graphical diffs
    - E.g. Munidiff -> PlantUML's Object Diagram diff
    - Previous experience: Picto [1]

<!-- _footer: "[1] https://eclipse.dev/epsilon/doc/picto/" -->

---

# Towards an Interoperable and Customisable Textual Format for Model Differences Reporting

Alfonso de la Vega (alfonso.delavega@unican.es)

<br>

Tools, demo and slides:

![w:300px](fig/qr.svg)

<!-- _footer: https://github.com/alfonsodelavega/modiff/tree/main/org.eclipse.epsilon.modiff.demo -->
