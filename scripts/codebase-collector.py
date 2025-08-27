import os
import subprocess
from pathlib import Path

def get_repo_root():
    """Get the root directory of the Git repository."""
    try:
        repo_root = subprocess.check_output(
            ['git', 'rev-parse', '--show-toplevel'],
            stderr=subprocess.STDOUT
        ).decode('utf-8').strip()
        return Path(repo_root)
    except subprocess.CalledProcessError:
        raise RuntimeError("This script must be run inside a Git repository.")

def find_src_files(root: Path, extensions: list[str]) -> list[Path]:
    """Find all files with given extensions in the src folder recursively."""
    src_dir = root / 'src'
    if not src_dir.exists():
        raise RuntimeError(f"src directory not found at {src_dir}")

    files = []
    for ext in extensions:
        files.extend(src_dir.rglob(f'*{ext}'))
    return files

def find_root_files(root: Path) -> list[Path]:
    """Find specific files at the repository root."""
    file_names = ['build.gradle', 'gradle.properties', 'settings.gradle', 'src/main/resources/vpp.accesswidener']
    files = []
    for fname in file_names:
        file_path = root / fname
        if file_path.exists():
            files.append(file_path)
    return files

def get_language(ext: str) -> str:
    """Get the markdown codeblock language based on extension."""
    if ext == '.java':
        return 'java'
    elif ext == '.json':
        return 'json'
    elif ext == '.gradle':
        return 'groovy'
    elif ext == '.properties':
        return 'properties'
    else:
        return ''

def create_codebase_md(root: Path, files: list[Path]):
    """Create the codebase.md file with frontmatter and paths and contents."""
    scratch_dir = root / '.scratch'
    scratch_dir.mkdir(exist_ok=True)

    md_path = scratch_dir / 'codebase.md'

    primer_path = root / 'ai-primers' / 'base-primer.md'
    if not primer_path.exists():
        raise RuntimeError(f"Primer file not found at {primer_path}")

    with open(md_path, 'w', encoding='utf-8') as md_file:
        # Write the frontmatter content first
        with open(primer_path, 'r', encoding='utf-8') as primer_file:
            md_file.write(primer_file.read())
            md_file.write("\n\n")  # Add some separation

        # Then write the file paths and codeblocks
        for file_path in files:
            relative_path = file_path.relative_to(root)
            ext = file_path.suffix
            lang = get_language(ext)

            # Write the relative path
            md_file.write(f"### {relative_path}\n\n")

            # Write the codeblock
            md_file.write(f"```{lang}\n")
            with open(file_path, 'r', encoding='utf-8') as src_file:
                md_file.write(src_file.read())
            md_file.write("\n```\n\n")

if __name__ == "__main__":
    repo_root = get_repo_root()
    src_files = find_src_files(repo_root, ['.java', '.json'])
    root_files = find_root_files(repo_root)
    all_files = sorted(root_files + src_files, key=lambda p: str(p.relative_to(repo_root)))
    create_codebase_md(repo_root, all_files)
    print(f"Created {repo_root / '.scratch' / 'codebase.md'}")
