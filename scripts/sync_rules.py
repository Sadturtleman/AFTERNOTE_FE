#!/usr/bin/env python3
"""
Cursor Rules to Claude Code Synchronization Script

This script creates CLAUDE.md that references Cursor rule files using @ syntax.
Claude Code will automatically import the referenced files.

Source of Truth: .cursor/rules/ directory
Output: CLAUDE.md (auto-generated, do not edit manually)
"""

import os
import re
import glob
from pathlib import Path
from typing import Dict, Tuple, List

PROJECT_ROOT = Path(__file__).parent.parent
CURSOR_RULES_FILE = PROJECT_ROOT / ".cursorrules"
CURSOR_RULES_DIR = PROJECT_ROOT / ".cursor" / "rules"
CLAUDE_MD_FILE = PROJECT_ROOT / "CLAUDE.md"


def parse_frontmatter(content: str) -> Tuple[Dict[str, str], str]:
    """Parse YAML frontmatter from .mdc file content."""
    frontmatter_pattern = r"^---\s*\n(.*?)\n---\s*\n(.*)$"
    match = re.match(frontmatter_pattern, content, re.DOTALL)
    
    if not match:
        return {}, content
    
    frontmatter_text = match.group(1)
    body = match.group(2)
    
    metadata = {}
    for line in frontmatter_text.split("\n"):
        if ":" in line:
            key, value = line.split(":", 1)
            key = key.strip()
            value = value.strip().strip('"').strip("'")
            metadata[key] = value
    
    return metadata, body


def get_file_priority(file_path: Path) -> int:
    """Determine priority for file ordering (lower = higher priority)."""
    priority_map = {
        "git-policy.mdc": 1,
        "branch-workflow.mdc": 2,
        "code-quality.mdc": 3,
        "architecture.mdc": 4,
        "compose.mdc": 5,
        "android-kotlin.mdc": 6,
    }
    
    filename = file_path.name
    return priority_map.get(filename, 99)


def get_relative_path(file_path: Path) -> str:
    """Get relative path from project root for @ reference."""
    return str(file_path.relative_to(PROJECT_ROOT)).replace("\\", "/")


def group_files_by_category(mdc_files: List[Path]) -> Dict[str, List[Path]]:
    """Group .mdc files by their parent directory (category)."""
    categories = {}
    for file_path in mdc_files:
        # Get parent directory name (workflow, tech-stack, quality)
        category = file_path.parent.name
        if category not in categories:
            categories[category] = []
        categories[category].append(file_path)
    
    # Sort files within each category by priority
    for category in categories:
        categories[category].sort(key=lambda p: (get_file_priority(p), str(p)))
    
    return categories


def create_claude_md_index() -> str:
    """Create CLAUDE.md as an index file that references Cursor rules using @ syntax."""
    output_lines = []
    
    # Header
    output_lines.append("# Project Context & Rules\n\n")
    output_lines.append(
        "**⚠️ WARNING: This file is auto-generated. Do not edit manually.**\n"
    )
    output_lines.append(
        "**To update rules, edit `.cursorrules` or `.cursor/rules/*.mdc` files.**\n\n"
    )
    output_lines.append(
        "This file uses Claude Code's `@` import syntax to reference Cursor rule files.\n"
    )
    output_lines.append(
        "Claude Code will automatically load the referenced files when this project is opened.\n\n"
    )
    output_lines.append("---\n\n")
    
    # 1. Core Rules (.cursorrules)
    if CURSOR_RULES_FILE.exists():
        output_lines.append("## Core Rules\n\n")
        output_lines.append(
            "Core project rules and critical guidelines:\n\n"
        )
        output_lines.append(f"- @{get_relative_path(CURSOR_RULES_FILE)}\n\n")
        output_lines.append("---\n\n")
    
    # 2. Detailed Rules by Category
    mdc_files = list(CURSOR_RULES_DIR.rglob("*.mdc"))
    if mdc_files:
        output_lines.append("## Detailed Guidelines\n\n")
        output_lines.append(
            "Please refer to the following specific rule files:\n\n"
        )
        
        # Group files by category
        categories = group_files_by_category(mdc_files)
        
        # Category display names
        category_names = {
            "workflow": "Workflow",
            "tech-stack": "Tech Stack",
            "quality": "Quality",
        }
        
        # Sort categories by priority
        category_priority = {"workflow": 1, "tech-stack": 2, "quality": 3}
        sorted_categories = sorted(
            categories.keys(),
            key=lambda c: category_priority.get(c, 99)
        )
        
        for category in sorted_categories:
            display_name = category_names.get(category, category.title())
            output_lines.append(f"### {display_name}\n\n")
            
            for mdc_file in categories[category]:
                # Get description from frontmatter if available
                try:
                    content = mdc_file.read_text(encoding="utf-8")
                    metadata, _ = parse_frontmatter(content)
                    description = metadata.get("description", "")
                    
                    file_name = mdc_file.stem.replace("-", " ").title()
                    relative_path = get_relative_path(mdc_file)
                    
                    if description:
                        output_lines.append(
                            f"- @{relative_path} - *{description}*\n"
                        )
                    else:
                        output_lines.append(f"- @{relative_path}\n")
                except Exception:
                    # If parsing fails, just add the file reference
                    relative_path = get_relative_path(mdc_file)
                    output_lines.append(f"- @{relative_path}\n")
            
            output_lines.append("\n")
        
        output_lines.append("---\n\n")
    
    # Footer
    output_lines.append("## How This Works\n\n")
    output_lines.append(
        "Claude Code uses the `@` syntax to import file contents. "
        "When you open this project in Claude Code, it will:\n\n"
    )
    output_lines.append("1. Read this `CLAUDE.md` file\n")
    output_lines.append("2. Automatically import all files referenced with `@`\n")
    output_lines.append("3. Load their contents into the context\n\n")
    output_lines.append(
        "This allows Cursor and Claude Code to share the same rule files "
        "without duplication or synchronization issues.\n"
    )
    
    return "".join(output_lines)


def main():
    """Main function to sync rules."""
    print("🔄 Creating CLAUDE.md index file with @ references...")
    
    # Check if source files exist
    if not CURSOR_RULES_FILE.exists() and not CURSOR_RULES_DIR.exists():
        print("❌ Error: No Cursor rules found!")
        print(f"   Expected: {CURSOR_RULES_FILE} or {CURSOR_RULES_DIR}")
        return 1
    
    # Generate CLAUDE.md
    try:
        claude_content = create_claude_md_index()
        CLAUDE_MD_FILE.write_text(claude_content, encoding="utf-8")
        print(f"✅ Successfully generated {CLAUDE_MD_FILE}")
        print(f"   Size: {len(claude_content)} characters")
        print(f"   This file references Cursor rules using @ syntax.")
        print(f"   Claude Code will automatically import the referenced files.")
        return 0
    except Exception as e:
        print(f"❌ Error generating CLAUDE.md: {e}")
        import traceback
        traceback.print_exc()
        return 1


if __name__ == "__main__":
    exit(main())
