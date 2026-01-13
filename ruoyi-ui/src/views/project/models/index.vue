<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模块名称" prop="moduleName">
        <el-input
          v-model="queryParams.moduleName"
          placeholder="请输入模块名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模块图标" prop="moduleIcon">
        <el-input
          v-model="queryParams.moduleIcon"
          placeholder="请输入模块图标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模块标识" prop="moduleKey">
        <el-input
          v-model="queryParams.moduleKey"
          placeholder="请输入模块标识(菜单路由)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['project:models:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['project:models:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['project:models:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['project:models:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="moduleId" />
      <el-table-column label="项目ID" align="center" prop="projectId" />
      <el-table-column label="模块排序" align="center" prop="moduleSort" />
      <el-table-column label="模块名称" align="center" prop="moduleName" />
      <el-table-column label="模块图标" align="center" prop="moduleIcon" />
      <el-table-column label="模块标识" align="center" prop="moduleKey" />
      <el-table-column label="模块描述" align="center" prop="moduleDescription" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['project:models:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['project:models:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改项目模块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属项目" prop="projectId">
              <el-select
                v-model="form.projectId"
                placeholder="请选择所属项目"
                style="width: 100%;"
              >
                <el-option
                  v-for="item in projectOptions"
                  :key="item.projectId"
                  :label="item.projectName"
                  :value="item.projectId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模块图标" prop="moduleIcon">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" :active-icon="form.moduleIcon" />
                <el-input slot="reference" v-model="form.moduleIcon" placeholder="点击选择图标" readonly>
                  <svg-icon
                    v-if="form.moduleIcon"
                    slot="prefix"
                    :icon-class="form.moduleIcon"
                    style="width: 25px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模块名称" prop="moduleName">
              <el-input v-model="form.moduleName" placeholder="请输入模块名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="模块标识" prop="moduleKey">
              <el-input v-model="form.moduleKey" placeholder="请输入模块标识(菜单路由)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="模块描述" prop="moduleDescription">
              <el-input v-model="form.moduleDescription" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listModels, getModels, delModels, addModels, updateModels } from "@/api/project/models"
import { listProject } from "@/api/project/project"
import IconSelect from "@/components/IconSelect"

export default {
  name: "Models",
  components: { IconSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 项目模块表格数据
      modelsList: [],
      // 项目选项
      projectOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectId: null,
        moduleSort: null,
        moduleName: null,
        moduleIcon: null,
        moduleKey: null,
        moduleDescription: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectId: [
          { required: true, message: "项目ID不能为空", trigger: "change" }
        ],
        moduleSort: [
          { required: true, message: "模块排序不能为空", trigger: "change" }
        ],
        moduleName: [
          { required: true, message: "模块名称不能为空", trigger: "blur" }
        ],
        moduleIcon: [
          { required: true, message: "模块图标不能为空", trigger: "blur" }
        ],
        moduleKey: [
          { required: true, message: "模块key(菜单路由)不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.moduleIcon = name
    },
    /** 查询项目列表 */
    getProjectList() {
      listProject().then(response => {
        this.projectOptions = response.rows;
      })
    },
    /** 查询项目模块列表 */
    getList() {
      this.loading = true
      listModels(this.queryParams).then(response => {
        this.modelsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        moduleId: null,
        projectId: null,
        moduleSort: null,
        moduleName: null,
        moduleIcon: null,
        moduleKey: null,
        moduleDescription: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.moduleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getProjectList()
      this.open = true
      this.title = "添加项目模块"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getProjectList()
      const moduleId = row.moduleId || this.ids
      getModels(moduleId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改项目模块"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.moduleId != null) {
            updateModels(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addModels(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const moduleIds = row.moduleId || this.ids
      this.$modal.confirm('是否确认删除项目模块编号为"' + moduleIds + '"的数据项？').then(function() {
        return delModels(moduleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('project/models/export', {
        ...this.queryParams
      }, `models_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
