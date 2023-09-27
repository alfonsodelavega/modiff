# Scenario notes

## s00

- A new `Status` element is added to `job2`
- The XMI report can be a bit misleading because the description of the job is also marked as changed (due to the change of the XML closing tag from `/>` to `>`

## s01

- The `description` attribute of `job1` changes
- Could be worse: classes with more attributes, no line break after each feature
- Munidiff would not have this issue because every feature is represented on their own lines

## s02

- A new secondary skill is added to `Alice`
- Unbounded uncontained reference: if many elements referenced, not that easy to spot the changes
- A combination of s03 and s04 can happen (less readable reports)
- On diffs over XMI models with line breaks after every feature, we might lose the context of the modified lines (e.g. if the element starting tag does not appear as a common line). By default, diff uses 3 common lines before and after changes. Classes with 10 features are not that uncommon!

## swapjob

- Slides example, `job1` moves from Alice's queue to Bob's
- A diff over XMI identifies as changing a set of lines surrounding the `job1` element, which is left as common line (e.g. "unchanged")