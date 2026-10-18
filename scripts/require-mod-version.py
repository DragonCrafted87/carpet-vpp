#!/usr/bin/env python
"""Fail a commit on a non-default branch whose mod_version matches the default branch."""

import re
import subprocess
import sys


def git(*args):
    result = subprocess.run(
        ["git", *args],
        check=False,
        capture_output=True,
        text=True,
    )
    return result


def mod_version(text, label):
    match = re.search(r"(?m)^mod_version=(.*)$", text)
    if match is None or not match.group(1).strip():
        sys.exit(f"{label} has no mod_version")
    return match.group(1).strip()


def default_ref():
    symbolic = git("symbolic-ref", "--short", "refs/remotes/origin/HEAD")
    if symbolic.returncode == 0 and symbolic.stdout.strip():
        return symbolic.stdout.strip()
    for candidate in ("origin/main", "origin/master", "main", "master"):
        if git("rev-parse", "--verify", "--quiet", candidate).returncode == 0:
            return candidate
    sys.exit("Cannot find the default branch to compare mod_version.")


def main():
    branch = git("rev-parse", "--abbrev-ref", "HEAD")
    if branch.returncode != 0:
        sys.exit(branch.stderr.strip() or "Cannot read the current branch.")
    current = branch.stdout.strip()
    base = default_ref()
    base_name = base.rsplit("/", 1)[-1]
    if current == base_name:
        return

    staged = git("show", ":gradle.properties")
    if staged.returncode != 0:
        sys.stderr.write(staged.stderr)
        sys.exit("gradle.properties is not in the index, so mod_version cannot be checked.")
    base_file = git("show", f"{base}:gradle.properties")
    if base_file.returncode != 0:
        sys.stderr.write(base_file.stderr)
        sys.exit(f"Cannot read gradle.properties from {base}.")

    staged_version = mod_version(staged.stdout, "the staged gradle.properties")
    base_version = mod_version(base_file.stdout, f"{base}:gradle.properties")
    if staged_version == base_version:
        sys.exit(
            f"Branch {current} must set mod_version to a value other than {base_version} "
            f"({base}). This branch is the place that changes the version."
        )


if __name__ == "__main__":
    main()
